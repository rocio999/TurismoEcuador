package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appturismo.data.database.model.Destino
import com.example.appturismo.data.database.repository.DestinoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DestinoViewModel : ViewModel() {

    private val _destinosApi =
        MutableStateFlow<List<Destino>>(emptyList())

    val destinosApi: StateFlow<List<Destino>> =
        _destinosApi

    private val _estado =
        MutableStateFlow<EstadoCarga>(EstadoCarga.Cargando)

    val estado: StateFlow<EstadoCarga> =
        _estado

    fun cargarDestinosApi() {

        if (_destinosApi.value.isNotEmpty()) {

            println(
                "DESTINOS YA CARGADOS: ${_destinosApi.value.size}"
            )

            return
        }

        viewModelScope.launch {

            _estado.value = EstadoCarga.Cargando

            try {

                println(
                    "PASO 1: CARGANDO DESTINOS DE ARC GIS"
                )

                val respuesta =
                    DestinoRepository.obtenerDestinosApi()

                val destinos =
                    respuesta.features.mapIndexed { index, feature ->

                        val datos = feature.attributes

                        val latitud =
                            feature.geometry?.y ?: 0.0

                        val longitud =
                            feature.geometry?.x ?: 0.0

                        // Obtenemos el nombre una sola vez
                        val nombreDestino =
                            datos["NOMBRE"]
                                ?.toString()
                                ?.uppercase()
                                ?: ""

                        // Asignamos la imagen según el nombre
                        val imagenDestino =
                            when {

                                nombreDestino.contains("QUILOTOA") ->
                                    com.example.appturismo.R.drawable.quilotoa

                                nombreDestino.contains("COTOPAXI") ->
                                    com.example.appturismo.R.drawable.cotopaxi

                                nombreDestino.contains("MONTAÑITA") ||
                                        nombreDestino.contains("MONTANITA") ->
                                    com.example.appturismo.R.drawable.montanita

                                else ->
                                    com.example.appturismo.R.drawable.quilotoa
                            }

                        Destino(

                            id = index + 1000,

                            nombre =
                                datos["NOMBRE"]?.toString()
                                    ?: "Sin nombre",

                            descripcion =
                                datos["TIPO"]?.toString()
                                    ?: "Destino turístico",

                            descripcioncompleta =
                                datos["DESCRIPCIO"]?.toString()
                                    ?: "Sin descripción disponible",

                            provincia =
                                datos["PROVINCIA"]?.toString()
                                    ?: "Sin provincia",

                            categoria =
                                datos["CATEGORIA"]?.toString()
                                    ?: "Sin categoría",

                            calificacion = 0.0,

                            imagen = imagenDestino,

                            imagenUrl = null,

                            latitud = latitud,

                            longitud = longitud
                        )
                    }

                _destinosApi.value = destinos

                _estado.value =
                    EstadoCarga.Exito

                println(
                    "DESTINOS API CARGADOS: ${destinos.size}"
                )

            } catch (e: Exception) {

                println(
                    "ERROR CARGANDO DESTINOS API: ${e.message}"
                )

                _estado.value =
                    EstadoCarga.Error(
                        e.message ?: "Error desconocido"
                    )
            }
        }
    }

    fun obtenerDestinos(): List<Destino> {

        return DestinoRepository.obtenerDestinos()
    }

    fun obtenerDestino(id: Int): Destino? {

        destinosApi.value.find {
            it.id == id
        }?.let {
            return it
        }

        return DestinoRepository.obtenerDestinoPorId(id)
    }
}

sealed class EstadoCarga {

    object Cargando : EstadoCarga()

    object Exito : EstadoCarga()

    data class Error(
        val mensaje: String
    ) : EstadoCarga()
}

