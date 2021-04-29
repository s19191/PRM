package pl.edu.pja.pysznepja

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.pysznepja.databinding.ActivityAddBinding
import pl.edu.pja.pysznepja.databinding.ActivityMainBinding

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawables = listOf<Int>(
            R.drawable.pierogi,
            R.drawable.pizza,
            R.drawable.rice,
            R.drawable.spaghetti,
            R.drawable.pumpkin,
            R.drawable.rosol
        ).map {
            ResourcesCompat.getDrawable(resources, it, theme)
//            resources.getDrawable(it, theme)
        }.filterNotNull()

        binding.images.apply {
            adapter = ImageAdapter(drawables)
            layoutManager = LinearLayoutManager(
                this@AddActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }
}