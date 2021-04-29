package pl.edu.pja.pysznepja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.edu.pja.pysznepja.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            println("code")
        }
    }
}