package pl.edu.pja.p02

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import pl.edu.pja.p02.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setResult(Activity.RESULT_CANCELED)

        val textSizeSpinner = binding.textSizeSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.textSize_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            textSizeSpinner.adapter = adapter
        }

//        val spinner = binding.textSizeSpinner
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.textSize_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//        }
    }

    fun onSave(view: View) {
        val intent = Intent().apply {
            putExtra("textSize", binding.textSizeSpinner.selectedItem.toString().toFloat())

        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun onBack(view: View) {
        finish()
    }
}