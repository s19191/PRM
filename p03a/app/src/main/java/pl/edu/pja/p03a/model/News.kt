package pl.edu.pja.p03a.model

import android.net.Uri
import java.time.LocalDateTime

data class News(
    val key: String,
    val newsTitle: String,
    val description: String,
    val link: String,
    val photo: Uri,
    val date: LocalDateTime,
    var read: Boolean
)