package pl.edu.pja.p02

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
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

const val MY_PERMISSIONS_REQUEST_CAMERA = 0

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val galleryAdapter by lazy { GalleryAdapter(this) }

    private var photoUri = Uri.EMPTY

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        textSize = 50f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shared.db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "travelerdb"
        ).build()
        setContentView(binding.root)
        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                }
            } else {
                openCamera()
            }
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
        }

        binding.settingsButton.setOnClickListener {
            startActivityForResult(Intent(this, SettingsActivity::class.java), SETTINGS_REQ)
        }

        setupPhotosList()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAM_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                val photoBitmap = getPhotoBitmap(photoUri)
                val resultBitmap: Bitmap = photoBitmap.copy(Bitmap.Config.ARGB_8888, true)

                val canvas = Canvas(resultBitmap)

                val calendar = Calendar.getInstance()
                val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
                val date = dateFormatter.format(calendar.time)

                canvas.drawText(date, 10f, canvas.height.toFloat() - 10f, paint)

                photoUri?.let { it ->
                    contentResolver.openOutputStream(it)?.use {
                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                }

                startActivityForResult(Intent(this, DescriptionActivity::class.java)
                    .putExtra("photoName", photoUri.toString()),
                    DESCRIPTION_REQ
                )

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    contentResolver.delete(photoUri, data?.extras)
                } else {
                    //TODO: Dodanie alternatywnego usuwania dla starszego antka
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DESCRIPTION_REQ) {
            if (resultCode == Activity.RESULT_CANCELED) {
                val traveler = TravelerDto(
                    description = null,
                    photoUri = photoUri.toString()
                )
                thread {
                    traveler?.let {
                        Shared.db?.travelers?.save(it)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}