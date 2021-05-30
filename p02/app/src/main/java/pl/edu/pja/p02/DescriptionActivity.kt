package pl.edu.pja.p02

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.p02.databinding.ActivityDescriptionBinding
import kotlin.concurrent.thread

class DescriptionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDescriptionBinding.inflate(layoutInflater) }
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
                        if (it.description != null) {
                            binding.description.setText(it.description)
                        }
                    }
                }
            }
        }
    }

    fun onSave(view: View) {
        var description = binding.description.text.toString()
        if (editItemId == 0L) {
            var photoUri = getPhotoName()
            thread {
                photoUri?.let {
                    Shared.db?.travelers?.update(it, description)
                }
            }
        } else {
            thread {
                Shared.db?.travelers?.update(editItemId, description)
            }
        }
        finish()
    }

    fun onCancel(view: View) {
        finish()
    }

    private fun getPhotoName() : String? {
        val bundle :Bundle ?= intent?.extras
        return if (bundle != null) {
            val photoUri = bundle.getString("photoUri")
            photoUri
        } else {
            null
        }
    }
}