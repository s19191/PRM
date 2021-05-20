package pl.edu.pja.p02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.p02.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}