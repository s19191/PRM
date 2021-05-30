package pl.edu.pja.p02

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import pl.edu.pja.p02.databinding.ActivityPhotoShowBinding
import kotlin.concurrent.thread

class PhotoShowActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPhotoShowBinding.inflate(layoutInflater) }
    private var editItemId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle :Bundle ?= intent.extras
        if (bundle != null) {
            editItemId = bundle.getLong("itemId")
        }

        if (editItemId != 0L) {
            thread {
                Shared.db?.travelers?.getById(editItemId)?.let { it ->
                    this.runOnUiThread {
                        binding.photoImageView.setImageBitmap(getPhotoBitmap(it.photoUri.toUri()))
                    }
                }
            }
        }
    }

    fun editDescription(view: View) {
        startActivity(Intent(this, DescriptionActivity::class.java).putExtra("itemId", editItemId))
    }

    private fun getPhotoBitmap(uri: Uri) : Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        }
    }
}