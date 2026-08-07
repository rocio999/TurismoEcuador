package com.example.appturismo.data.database.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class WikimediaResponse(
    val batchcomplete: Boolean?,
    val query: WikimediaQuery?
)

data class WikimediaQuery(
    val pages: Map<String, WikimediaImage>?
)

data class WikimediaImage(
    val pageid: Long,
    val ns: Int?,
    val title: String?,
    val imageinfo: List<WikimediaImageInfo>?
)

data class WikimediaImageInfo(
    val url: String?,
    val thumburl: String?
)

interface ImagenApiService {

    @GET("w/api.php")
    suspend fun buscarImagenes(
        @Query("action") action: String = "query",
        @Query("generator") generator: String = "geosearch",
        @Query("ggsprimary") primary: String = "all",
        @Query("ggsnamespace") namespace: Int = 6,
        @Query("ggsradius") radius: Int = 10000,
        @Query("ggslimit") limit: Int = 50,
        @Query("ggscoord") coordenadas: String,
        @Query("prop") prop: String = "imageinfo",
        @Query("iiprop") imageInfo: String = "url",
        @Query("iiurlwidth") imageWidth: Int = 600,
        @Query("format") format: String = "json",
        @Query("origin") origin: String = "*"
    ): WikimediaResponse
}