package pl.edu.pja.p03a.model

import android.net.Uri

data class News(
    val newsTitle: String?,
    val description: String?,
    val link: String?,
    val photo: Uri?
)