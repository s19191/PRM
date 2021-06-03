package pl.edu.pja.p02

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
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
import android.os.Looper
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
import java.io.File
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

    //TODO: brodcastreciver na akcje bootcompleted

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
        }
    }

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

        checkLocationPermissions()

        setupPhotosList()

        geofencingClient = LocationServices.getGeofencingClient(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        registerChannel()

        startLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        val uris = getUris()
        thread {
            val newList: MutableList<Traveler> = mutableListOf()
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

    private fun getUris() : MutableList<Uri> {
        val uris: MutableList<Uri> = mutableListOf()
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
        return uris
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper())
        }
    }

    private fun setupPhotosList() {
        binding.photosList.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    fun onCamera(view: View) {
        if (checkCameraPermissions()) {
            openCamera()
        }
    }

    fun onSettings(view: View) {
        val color = prefs.getInt("textColor", 0)
        val size = prefs.getInt("textSize", 4)
        val radius = prefs.getFloat("radius", 1000f)
        val settingsIntent = Intent(this, SettingsActivity::class.java).apply {
            putExtra("textColor", color)
            putExtra("textSize", size)
            putExtra("radius", radius)
        }
        startActivityForResult(settingsIntent, SETTINGS_REQ)
    }

    fun openEditActivity(itemId: Long, photoUri: String) {
        val photoShowIntent = Intent(this, PhotoShowActivity::class.java).apply {
            putExtra("itemId", itemId)
            putExtra("photoUri", photoUri)
        }
        startActivity(photoShowIntent)
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun checkCameraPermissions() : Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA
            )
            false
        } else {
            true
        }
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
        startActivityForResult(
            Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        ).putExtra(
            MediaStore.EXTRA_OUTPUT,
            photoUri
        ),
            CAM_REQ
        )
    }

    private fun getPhotoBitmap(uri: Uri) : Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
    }

    private fun savePhoto(photoUri: Uri, photoBitmap: Bitmap, latitude: Double?, longitude: Double?) {
        photoUri.let { it ->
            contentResolver.openOutputStream(it)?.use {
                photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }
        val traveler = TravelerDto(
            description = null,
            photoUri = photoUri.toString(),
            latitude = latitude,
            longitude = longitude
        )
        thread {
            traveler.let { it ->
                Shared.db?.travelers?.save(it)
                if (latitude != null && longitude != null) {
                    Shared.db?.travelers?.getByPhotoUri(photoUri.toString())
                        .let {
                            setGeofence(
                                it?.id?.toInt()!!,
                                latitude,
                                longitude
                            )
                        }
                }
            }
        }
    }

    fun deletePhoto(photoUri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            contentResolver.delete(photoUri, this.intent.extras)
        } else {
            val file = File(photoUri.path)
            file.delete()
            if (file.exists()) {
                file.canonicalFile.delete()
                if (file.exists()) {
                    applicationContext.deleteFile(file.name)
                }
            }
        }
    }

    private fun setGeofence(requestCode: Int, latitude: Double, longitude: Double) {
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
            Intent(this, Notifier::class.java).putExtra("requestCode", requestCode),
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
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        savePhoto(photoUri, resultBitmap, null, null)
                        startActivity(
                            Intent(
                                this,
                                DescriptionActivity::class.java
                            )
                                .putExtra(
                                    "photoUri",
                                    photoUri.toString()
                                )
                        )
                    } else {
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
                                savePhoto(photoUri, resultBitmap, location.latitude, location.longitude)
                            }.let {
                                startActivity(
                                    Intent(
                                        this,
                                        DescriptionActivity::class.java
                                    )
                                        .putExtra(
                                            "photoUri",
                                            photoUri.toString()
                                        )
                                )
                            }
                    }
            } else {
                deletePhoto(photoUri)
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
}