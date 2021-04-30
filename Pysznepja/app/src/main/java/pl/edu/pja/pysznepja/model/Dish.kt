package pl.edu.pja.pysznepja.model

import android.graphics.drawable.Drawable

data class Dish(
    val drawable: Drawable,
    val name: String,
    val ingredients: List<String>
)