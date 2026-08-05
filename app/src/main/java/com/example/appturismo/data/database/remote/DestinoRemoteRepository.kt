package com.example.appturismo.data.database.remote


class DestinoRemoteRepository {

    suspend fun obtenerDestinos(): DestinoApiResponse {
        return RetrofitClient.apiService.obtenerDestinos()
    }
}