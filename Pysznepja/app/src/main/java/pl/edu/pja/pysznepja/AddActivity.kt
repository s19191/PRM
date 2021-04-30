package pl.edu.pja.pysznepja

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.pja.pysznepja.adapter.ImageAdapter
import pl.edu.pja.pysznepja.databinding.ActivityAddBinding
import pl.edu.pja.pysznepja.model.Dish

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }

    private val drawables by lazy {
        listOf<Int>(
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
    }

    private val imageAdapter by lazy { ImageAdapter(drawables) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupImages()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun setupImages() {
        binding.images.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(
                this@AddActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    fun onSave(view: View) {
        val name = binding.name.text.toString()
        val ingredients = binding.ingredients.text.toString().split("\n")
        val drawable = imageAdapter.selectedItem?.let {
            drawables[it] }
        if (drawable == null || name.isEmpty() || ingredients.isEmpty()) {
            Toast.makeText(this, "Nie wybrałeś miniaturki", Toast.LENGTH_LONG).show()
            return
        }
        val dish = Dish(drawable, name, ingredients)
        Shared.dishList.add(dish)

        val intent = Intent().apply {
            putExtra("name", name)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}