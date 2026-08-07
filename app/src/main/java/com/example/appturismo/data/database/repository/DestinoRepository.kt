
package com.example.appturismo.data.database.repository

import com.example.appturismo.R
import com.example.appturismo.data.database.model.Destino
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

    suspend fun obtenerDestinosApi() =
        RetrofitClient.apiService.obtenerDestinos()
}
