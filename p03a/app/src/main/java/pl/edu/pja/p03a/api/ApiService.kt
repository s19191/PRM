package pl.edu.pja.p03a.api

import pl.edu.pja.p03a.model.Rss
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("pub/rss/wiadomosci.htm")
    suspend fun getItems(): Response<Rss>
}