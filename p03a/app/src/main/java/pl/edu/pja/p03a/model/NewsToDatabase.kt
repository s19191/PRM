package pl.edu.pja.p03a.model

data class NewsToDatabase(
    val newsTitle: String,
    val description: String,
    val link: String,
    val photo: String,
    val read: Boolean
)
{
    constructor() : this("", "", "", "", false)
}