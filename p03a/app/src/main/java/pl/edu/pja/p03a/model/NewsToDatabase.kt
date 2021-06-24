package pl.edu.pja.p03a.model

data class NewsToDatabase(
    val newsTitle: String,
    val description: String,
    val link: String,
    val photo: String,
    val date: Long,
    val read: Boolean,
    val fav: Boolean
)
{
    constructor() : this("", "", "", "", 0, false, false)
}