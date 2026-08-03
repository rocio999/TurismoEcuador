package com.example.appturismo.data.database.remote



data class DestinoApiResponse(
    val features: List<DestinoApiFeature>
)

data class DestinoApiFeature(
    val attributes: DestinoApi
)