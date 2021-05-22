package pl.edu.pja.p02.model

import android.graphics.Bitmap
import android.net.Uri

data class Traveler(
    val id: Long,
    val description: String?,
    val photoBitmap: Bitmap
)
