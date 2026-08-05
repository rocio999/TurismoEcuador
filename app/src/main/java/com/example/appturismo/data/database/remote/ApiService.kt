package com.example.appturismo.data.database.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("FeatureServer/0/query")
    suspend fun obtenerDestinos(
        @Query("where") where: String = "1=1",
        @Query("outFields") outFields: String = "*",
        @Query("f") format: String = "json"
    ): DestinoApiResponse
}