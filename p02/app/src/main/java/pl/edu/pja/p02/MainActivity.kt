package pl.edu.pja.p02

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import pl.edu.pja.p02.adapter.PhotoAdapter
//import pl.edu.pja.p02.adapter.PhotoAdapter
import pl.edu.pja.p02.databinding.ActivityMainBinding
import pl.edu.pja.p02.model.Traveler
import pl.edu.pja.p02.model.TravelerDto
import java.text.DateFormat
import java.util.*
import kotlin.concurrent.thread


const val REQ = 1
const val REQ01 = 2

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val photoAdapter by lazy { PhotoAdapter() }

    private var imgUri = Uri.EMPTY

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        textSize = 50f
    }

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shared.db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "travelerdb"
        ).build()
        setContentView(binding.root)

        binding.cameraButton.setOnClickListener {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "traveler.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
            imgUri = contentResolver?.insert(
                uri,
                contentValues
            )
            println(imgUri)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .let {
                it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            }
            startActivityForResult(cameraIntent, REQ)
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupExpenseList()
    }

    private fun setupExpenseList() {
        binding.photosList.apply {
            adapter = photoAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    override fun onResume() {
        super.onResume()
        thread {
        Shared.db?.travelers?.getAll()?.let { it ->
            val newList = it.map {
                val photoBitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(this.contentResolver, it.photoName.toUri())
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, it.photoName.toUri())
                }
                Traveler(
                    it.id,
                    it.description,
                    photoBitmap
                )
            }
            photoAdapter.travelers = newList
        }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ) {
            if (resultCode == Activity.RESULT_OK) {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                val tmpImage = data.extras?.get("data") as Bitmap
//                imgUri?.let { it ->
//                    contentResolver.openOutputStream(it)?.use {
//                        tmpImage.compress(Bitmap.CompressFormat.JPEG, 100, it)
//                    }
//                }
                val photoBitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(this.contentResolver, imgUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
                }
                val resultBitmap: Bitmap = photoBitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(resultBitmap)

                val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
                val date = dateFormatter.format(calendar.time)

                canvas.drawText(date, 10f, canvas.height.toFloat() - 10f, paint)

                imgUri?.let { it ->
                    contentResolver.openOutputStream(it)?.use {
                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                }

                startActivityForResult(Intent(this, DescriptionActivity::class.java).putExtra("photoName", imgUri.toString()), REQ01)

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    contentResolver.delete(imgUri, data?.extras)
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ01) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val traveler = TravelerDto(
                        description = null,
                        photoName = imgUri.toString()
                    )
                    thread {
                        traveler?.let {
                            Shared.db?.travelers?.save(it)
                        }
                    }
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}