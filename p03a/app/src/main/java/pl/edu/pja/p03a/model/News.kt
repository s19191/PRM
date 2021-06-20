package pl.edu.pja.p03a.model

import android.graphics.drawable.Drawable
import android.net.Uri
import java.net.URL

data class News(
    val newsTitle: String?,
    val description: String?,
    val link: String?,
    val photo: Uri?
)