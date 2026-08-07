package com.example.appturismo.data.database.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object ImagenApiClient {

    private const val BASE_URL =
        "https://commons.wikimedia.org/"

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .header(
                    "User-Agent",
                    "AppTurismo/1.0 (Android)"
                )
                .build()

            chain.proceed(request)
        }
        .build()

    val apiService: ImagenApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImagenApiService::class.java)
    }
}