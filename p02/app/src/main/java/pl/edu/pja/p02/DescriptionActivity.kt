package pl.edu.pja.p02

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.p02.databinding.ActivityDescriptionBinding
import pl.edu.pja.p02.model.TravelerDto
import kotlin.concurrent.thread

class DescriptionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDescriptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setResult(Activity.RESULT_CANCELED)
    }

    fun onSave(view: View) {
        var description = binding.description.text.toString()
        if (description.isEmpty()) {
            description = null.toString()
        }
        var photoUri = getPhotoName()
        val traveler = photoUri?.let {
            TravelerDto(
                description = description,
                photoName = it
            )
        }
        thread {
            traveler?.let {
                Shared.db?.travelers?.save(it)
            }
        }
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun onCancel(view: View) {
        var photoUri = getPhotoName()
        val traveler = photoUri?.let {
            TravelerDto(
                description = null,
                photoName = it
            )
        }
        thread {
            traveler?.let {
                Shared.db?.travelers?.save(it)
            }
        }
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getPhotoName() : String? {
        val bundle :Bundle ?= intent?.extras
        return if (bundle != null) {
            var photoUri = bundle.getString("photoName")
            photoUri
        } else {
            null;
        }
    }
}