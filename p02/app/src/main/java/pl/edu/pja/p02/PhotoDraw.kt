package pl.edu.pja.p02

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PhotoDraw(context: Context, attrs: AttributeSet, photo: Bitmap) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        textSize = 20f
    }

    fun onDraw(canvas: Canvas?, photo: Bitmap) {
        canvas?.let {
            canvas
        }
    }
}