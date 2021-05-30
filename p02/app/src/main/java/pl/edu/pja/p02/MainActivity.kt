package pl.edu.pja.p02

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.android.gms.location.*
import pl.edu.pja.p02.adapter.GalleryAdapter
import pl.edu.pja.p02.databinding.ActivityMainBinding
import pl.edu.pja.p02.model.Traveler
import pl.edu.pja.p02.model.TravelerDto
import java.text.DateFormat
import java.util.*
import kotlin.concurrent.thread

const val CAM_REQ = 1
const val SETTINGS_REQ = 2

const val MY_PERMISSIONS_REQUEST_CAMERA = 100
const val MY_PERMISSIONS_REQUEST_LOCATION = 99

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val galleryAdapter by lazy { GalleryAdapter(this) }

    private var photoUri = Uri.EMPTY

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val prefs by lazy { getSharedPreferences("prefs", Context.MODE_PRIVATE) }

    lateinit var geofencingClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shared.db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "travelerdb"
        ).build()
        setContentView(binding.root)
        setupPhotosList()


        //TODO: Nie pyta o lokalizację w tle, która by była wymagana
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }

        geofencingClient = LocationServices.getGeofencingClient(this)

        registerChannel()
    }

    override fun onResume() {
        super.onResume()
        var uris: MutableList<Uri> = mutableListOf()
        val filers = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        contentResolver.query(
            filers,
            arrayOf(MediaStore.Images.Media._ID),
            null,
            null,
            null
        )?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex(MediaStore.Images.Media._ID))
                val imgUri = ContentUris.withAppendedId(filers, id.toLong())
                uris.add(imgUri)
            }
        }
        thread {
            var newList: MutableList<Traveler> = mutableListOf()
            uris.forEach { uri ->
                val photoBitmap = getPhotoBitmap(uri)
                Shared.db?.travelers?.getByPhotoUri(uri.toString())?.let { tDto ->
                    Traveler(
                        tDto.id,
                        tDto.description,
                        photoBitmap,
                        uri
                    ).let {
                        newList.add(it)
                    }
                }
            }
            galleryAdapter.travelers = newList
        }
    }

    private fun setupPhotosList() {
        binding.photosList.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    fun onCamera(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    fun onSettings(view: View) {
        val color = prefs.getInt("textColor", 0)
        val size = prefs.getInt("textSize", 4)
        val radius = prefs.getInt("radius", 1000)
        val settingsIntent = Intent(this, SettingsActivity::class.java).apply {
            putExtra("textColor", color)
            putExtra("textSize", size)
            putExtra("radius", radius)
        }
        startActivityForResult(settingsIntent, SETTINGS_REQ)
    }

    private fun openCamera() {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "traveler.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = contentResolver?.insert(
            uri,
            contentValues
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
            it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        startActivityForResult(cameraIntent, CAM_REQ)
    }


    //TODO: Tu coś może psuć coś
    private fun getPhotoBitmap(uri: Uri) : Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            println(uri)
            val source = ImageDecoder.createSource(this.contentResolver, uri)
//            println(source)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
    }

    private fun setGeofence(requestCode: Int, latitude: Double, longitude: Double) {
        println(requestCode)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            geofencingClient.addGeofences(
                generateRequest(latitude, longitude),
                generatePendingIntent(requestCode)
            )
        }
    }

    private fun generateRequest(latitude: Double, longitude: Double): GeofencingRequest {
        val geofence = Geofence.Builder()
            .setCircularRegion(latitude, longitude, prefs.getFloat("radius", 1000f))
            .setRequestId(photoUri.toString())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
            .build()
    }

    private fun generatePendingIntent(requestCode: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            applicationContext,
            requestCode,
            Intent(this, Notifier::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun registerChannel() {
        getSystemService(NotificationManager::class.java).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    "pl.edu.pja.p02.Geofence",
                    "Geofences",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                it.createNotificationChannel(notificationChannel)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAM_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                val photoBitmap = getPhotoBitmap(photoUri)
                val resultBitmap: Bitmap = photoBitmap.copy(Bitmap.Config.ARGB_8888, true)

                val canvas = Canvas(resultBitmap)

                val calendar = Calendar.getInstance()
                val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
                val date = dateFormatter.format(calendar.time)

                val textSizeTmp = Shared.sizes[prefs.getInt("textSize", 4)]
                val textColorTmp = Shared.colors[prefs.getInt("textColor", 0)]

                val paint = Paint().apply {
                    color = textColorTmp
                    textSize = textSizeTmp
                }

                canvas.drawText(date, 10f, canvas.height.toFloat() - 10f, paint)

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    this.runOnUiThread {
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                val geocoder = Geocoder(this, Locale.getDefault())
                                val addresses: List<Address> = geocoder.getFromLocation(
                                    location.latitude,
                                    location.longitude,
                                    1
                                )
                                val cityName: String = addresses[0].locality
                                val countryName: String = addresses[0].countryName
                                val tmp = "$cityName $countryName"
                                canvas.drawText(
                                    tmp,
                                    10f,
                                    canvas.height.toFloat() - textSizeTmp - 10f,
                                    paint
                                )

                                photoUri?.let { it ->
                                    contentResolver.openOutputStream(it)?.use {
                                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                                    }
                                }

                                val traveler = TravelerDto(
                                    description = null,
                                    photoUri = photoUri.toString(),
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                                thread {
                                    traveler?.let {
                                        Shared.db?.travelers?.save(it)
                                    }
                                }.let {
                                    thread {
                                        Shared.db?.travelers?.getByPhotoUri(photoUri.toString())
                                            .let {
                                                setGeofence(
                                                    it?.id?.toInt()!!,
                                                    location.latitude,
                                                    location.longitude
                                                )
                                            }
                                    }
                                }
                            }.let {
                                val descriptionIntent =
                                    Intent(this, DescriptionActivity::class.java).let {
                                        it.putExtra("photoUri", photoUri.toString())
                                    }
                                startActivity(descriptionIntent)
                            }
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    contentResolver.delete(photoUri, data?.extras)
                } else {
                    //TODO: Dodanie alternatywnego usuwania dla starszego antka
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SETTINGS_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getLongExtra("textSize", 50).let {
                    prefs.edit()
                        .putInt("textSize", it!!.toInt())
                        .apply()
                }
                data?.getLongExtra("textColor", 0).let {
                    prefs.edit()
                        .putInt("textColor", it!!.toInt())
                        .apply()
                }
                data?.getFloatExtra("radius", 0f).let {
                    if (it != 0f) {
                        prefs.edit()
                            .putFloat("radius", it!!)
                            .apply()
                    }
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.CAMERA
//                ) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                openCamera()
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
}