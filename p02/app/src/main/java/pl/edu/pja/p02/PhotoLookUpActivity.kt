package pl.edu.pja.p02

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import pl.edu.pja.p02.databinding.ActivityPhotoLookUpBinding
import kotlin.concurrent.thread

class PhotoLookUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPhotoLookUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle :Bundle ?= intent.extras
        bundle?.getString("photoUri")?.let { photoUri ->
            thread {
                Shared.db?.travelers?.getByPhotoUri(photoUri)?.let { it ->
                    this.runOnUiThread {
                        binding.descriptionPhotoLookUp.text = it.description
                        binding.photoLookUp.setImageBitmap(getPhotoBitmap(photoUri.toUri()))
                    }
                }
            }
        }
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