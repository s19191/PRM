package pl.edu.pja.p02

import android.app.Activity
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
        val bundle :Bundle ?= intent?.extras
        if (bundle != null) {
            val description = binding.description.text.toString()
            var photoUri = bundle.getString("photoName")
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
        finish()
    }
}