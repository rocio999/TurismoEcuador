package com.example.appturismo.ui.theme.repository


import com.example.appturismo.R
import com.example.appturismo.ui.theme.model.Destino


object DestinoRepository {

    fun obtenerDestinos(): List<Destino> {

        return listOf(

            Destino(
                nombre = "🏔️ Quilotoa",
                descripcion = "Laguna de origen volcánico",
                provincia = "Cotopaxi",
                categoria = "Laguna",
                calificacion = 4.9,
                imagen = R.drawable.quilotoa
            ),

            Destino(
                nombre = "🌋 Cotopaxi",
                descripcion = "Parque Nacional",
                provincia = "Cotopaxi",
                categoria = "Volcán",
                calificacion = 4.8,
                imagen = R.drawable.cotopaxi
            ),

            Destino(
                nombre = "🌊 Montañita",
                descripcion = "Playa del Ecuador",
                provincia = "Santa Elena",
                categoria = "Playa",
                calificacion = 4.7,
                imagen = R.drawable.montanita
            )

        )

    }

}