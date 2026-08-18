package com.example.appturismo.data.database.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Obtener todos los destinos
    @GET("FeatureServer/0/query")
    suspend fun obtenerDestinos(
        @Query("where") where: String = "1=1",
        @Query("outFields") outFields: String = "*",
        @Query("returnGeometry") returnGeometry: Boolean = true,
        @Query("f") format: String = "json"
    ): DestinoApiResponse


    // Obtener destinos cercanos a una ubicación
    @GET("FeatureServer/0/query")
    suspend fun obtenerDestinosCercanos(

        @Query("geometry")
        geometry: String,

        @Query("geometryType")
        geometryType: String = "esriGeometryPoint",

        @Query("inSR")
        inSR: Int = 4326,

        @Query("spatialRel")
        spatialRel: String = "esriSpatialRelIntersects",

        @Query("distance")
        distance: Double = 20.0,

        @Query("units")
        units: String = "esriSRUnit_Kilometer",

        @Query("outFields")
        outFields: String = "*",

        @Query("returnGeometry")
        returnGeometry: Boolean = true,

        @Query("f")
        format: String = "json"
    ): DestinoApiResponse
}