package com.example.appturismo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appturismo.data.database.remote.ImagenApiClient
import kotlinx.coroutines.launch

class ImagenTestViewModel : ViewModel() {

    fun probarImagen() {

        viewModelScope.launch {

            println("PASO 1: INICIANDO")

            try {

                println("PASO 2: ANTES DE LLAMAR A WIKIMEDIA")

                val latitud = -33.051806346
                val longitud = -71.657897043

                val coordenadas = "$latitud|$longitud"

                println("PASO 3: COORDENADAS = $coordenadas")

                val respuesta =
                    ImagenApiClient.apiService.buscarImagenes(
                        coordenadas = coordenadas
                    )

                println("PASO 4: RESPUESTA RECIBIDA")

                println("RESULTADO_WIKI_COMPLETO = $respuesta")

                val paginas =
                    respuesta.query?.pages ?: emptyMap()

                println("PASO 5: TOTAL = ${paginas.size}")

            } catch (e: Exception) {

                Log.e(
                    "WIKIMEDIA_ERROR",
                    "Ocurrió un error",
                    e
                )
            }
        }
    }

    suspend fun obtenerImagenUrl(
        latitud: Double,
        longitud: Double
    ): String? {

        return try {

            val coordenadas = "$latitud|$longitud"

            val respuesta =
                ImagenApiClient.apiService.buscarImagenes(
                    coordenadas = coordenadas
                )

            val paginas =
                respuesta.query?.pages ?: emptyMap()

            val primeraImagen =
                paginas.values.firstOrNull()

            primeraImagen
                ?.imageinfo
                ?.firstOrNull()
                ?.thumburl

        } catch (e: Exception) {

            Log.e(
                "WIKIMEDIA_ERROR",
                "Error buscando imagen",
                e
            )

            null
        }
    }
}