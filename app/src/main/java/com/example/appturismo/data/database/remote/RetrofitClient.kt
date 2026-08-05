package com.example.appturismo.data.database.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://services8.arcgis.com/5BaAHTQ4nRVz57H5/arcgis/rest/services/Atractivos_Tur%C3%ADsticos_Nacionales/"

    val apiService: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}