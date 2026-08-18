package com.example.appturismo.data.database.repository

import com.example.appturismo.R
import com.example.appturismo.data.database.model.Destino
import com.example.appturismo.data.database.remote.DestinoApiResponse
import com.example.appturismo.data.database.remote.RetrofitClient

object DestinoRepository {

    fun obtenerDestinoPorId(id: Int): Destino? {
        return obtenerDestinos().find {
            it.id == id
        }
    }

    fun obtenerDestinos(): List<Destino> {
        return listOf(
            Destino(
                id = 1,
                nombre = "🏔️ Quilotoa",
                descripcion = "Laguna de origen volcánico",
                descripcioncompleta = "Quilotoa es una impresionante laguna de origen volcánico ubicada en la provincia de Cotopaxi. Sus aguas color turquesa y los paisajes andinos la convierten en uno de los destinos turísticos más visitados del Ecuador.",
                provincia = "Cotopaxi",
                categoria = "Laguna",
                calificacion = 4.9,
                imagen = R.drawable.quilotoa,
                imagenUrl = null,
                latitud = -0.8600,
                longitud = -78.9070
            ),
            Destino(
                id = 2,
                nombre = "🌋 Cotopaxi",
                descripcion = "Parque Nacional",
                descripcioncompleta = "El volcán Cotopaxi es un gran destino de viaje en Ecuador por su cono con nieve casi perfecto, su altura de 5.897 metros y su cercanía a Quito. El sitio destaca por atractivos como la Laguna de Limpiopungo, el Refugio José Rivas y sus amplios páramos andinos.",
                provincia = "Cotopaxi",
                categoria = "Volcán",
                calificacion = 4.8,
                imagen = R.drawable.cotopaxi,
                imagenUrl = null,
                latitud = -0.6770,
                longitud = -78.4370
            ),
            Destino(
                id = 3,
                nombre = "🌊 Montañita",
                descripcion = "Playa del Ecuador",
                descripcioncompleta = "Montañita es un famoso pueblo playero en la provincia de Santa Elena, conocido por el surf, su ambiente turístico y sus playas del Pacífico.",
                provincia = "Santa Elena",
                categoria = "Playa",
                calificacion = 4.7,
                imagen = R.drawable.montanita,
                imagenUrl = null,
                latitud = -1.8260,
                longitud = -80.7530
            )
        )
    }

    // ============================================================
    // LLAMADAS A LA API
    // ============================================================

    suspend fun obtenerDestinosApi(): DestinoApiResponse =
        RetrofitClient.apiService.obtenerDestinos()

    suspend fun obtenerDestinosCercanosApi(
        latitud: Double,
        longitud: Double,
        radioKm: Double = 20.0
    ): DestinoApiResponse =
        RetrofitClient.apiService.obtenerDestinosCercanos(
            geometry = "$longitud,$latitud",
            distance = radioKm
        )

    // ============================================================
    // CÁLCULO DE DISTANCIA HAVERSINE (LOCAL/RESPALDO)
    // ============================================================

    fun calcularDistanciaKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val radioTierra = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return radioTierra * c
    }
    fun obtenerDestinosCercanos(
        latitud: Double,
        longitud: Double,
        radioKm: Double = 100.0
    ): List<Destino> {

        return obtenerDestinos().filter { destino ->

            val distancia = calcularDistanciaKm(
                latitud,
                longitud,
                destino.latitud,
                destino.longitud
            )

            distancia <= radioKm
        }
    }
}