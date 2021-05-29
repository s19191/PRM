package pl.edu.pja.p02

import android.graphics.Color

object Shared {
    var db: AppDatabase? = null
    var sizes: MutableList<Float> = mutableListOf(
        10f,
        20f,
        30f,
        40f,
        50f,
        60f,
        70f,
        80f,
        90f,
        100f,
        110f,
        120f,
        130f,
        140f,
        150f
    )
    val colors: MutableList<Int> = mutableListOf(
        Color.BLACK,
        Color.BLUE,
        Color.CYAN,
        Color.LTGRAY,
        Color.DKGRAY,
        Color.GRAY,
        Color.GREEN,
        Color.MAGENTA,
        Color.RED,
        Color.WHITE,
        Color.YELLOW
    )

}