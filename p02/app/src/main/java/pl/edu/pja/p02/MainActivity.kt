package pl.edu.pja.p02

import android.Manifest
import android.app.Activity
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
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import pl.edu.pja.p02.adapter.GalleryAdapter
import pl.edu.pja.p02.databinding.ActivityMainBinding
import pl.edu.pja.p02.model.Traveler
import pl.edu.pja.p02.model.TravelerDto
import java.text.DateFormat
import java.util.*
import kotlin.concurrent.thread

const val CAM_REQ = 1
const val DESCRIPTION_REQ = 2
const val SETTINGS_REQ = 3

const val MY_PERMISSIONS_REQUEST_CAMERA = 1
const val MY_PERMISSIONS_REQUEST_LOCATION = 2

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val galleryAdapter by lazy { GalleryAdapter(this) }

    private var photoUri = Uri.EMPTY

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val prefs by lazy { getSharedPreferences("prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shared.db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "travelerdb"
        ).build()
        setContentView(binding.root)
            //TODO: Możliwość pobierania zdjęć od razu z pamięci (MediaStore)
//            val filers = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//            val cursor = contentResolver.query(filers, null, null, null, null)
//            contentResolver.query(
//                filers,
//                arrayOf(MediaStore.Images.Media._ID),
//                null,
//                null,
//                null
//            )?.use {
//                while (it.moveToNext()) {
//                    val id = it.getInt(it.getColumnIndex(MediaStore.Images.Media._ID))
//                    val imgUri = ContentUris.withAppendedId(filers, id.toLong())
//                    imageUri = imgUri
//                    contentResolver.openInputStream(imgUri)?.use {
//                        BitmapFactory.decodeStream(it).let {
//                            view.imageView.setImageBitmap(it)
//                        }
//                    }
//                }
//            }
        setupPhotosList()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
            Shared.db?.travelers?.getAll()?.let { it ->
                val newList = it.map {
                    val photoBitmap = getPhotoBitmap(it.photoUri.toUri())
                    Traveler(
                        it.id,
                        it.description,
                        photoBitmap,
                        it.photoUri.toUri()
                    )
                }
                galleryAdapter.travelers = newList.toMutableList()
            }
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
            openCamera()
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

    private fun getPhotoBitmap(uri: Uri) : Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
    }

    private fun setupGeofences() {

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
                data?.getIntExtra("radius", 0).let {
                    if (it != 0) {
                        prefs.edit()
                            .putInt("radius", it!!)
                            .apply()
                    }
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}