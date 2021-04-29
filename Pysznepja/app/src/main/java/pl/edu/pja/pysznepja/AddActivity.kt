package pl.edu.pja.pysznepja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.edu.pja.pysznepja.databinding.ActivityAddBinding
import pl.edu.pja.pysznepja.databinding.ActivityMainBinding

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add)
    }
}