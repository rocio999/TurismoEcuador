package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appturismo.data.database.model.Destino
import com.example.appturismo.data.database.repository.DestinoRepository
import com.example.appturismo.data.database.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DestinoViewModel : ViewModel() {

    private val _destinosApi = MutableStateFlow<List<Destino>>(emptyList())
    val destinosApi: StateFlow<List<Destino>> = _destinosApi

    fun cargarDestinosApi() {

        viewModelScope.launch {

            try {

                val respuesta = RetrofitClient.apiService.obtenerDestinos()

                val destinos = respuesta.features.mapIndexed { index, feature ->

                    val datos = feature.attributes

                    Destino(
                        id = index + 1000,
                        nombre = datos["NOMBRE"]?.toString() ?: "Sin nombre",
                        descripcion = datos["TIPO"]?.toString() ?: "Destino turístico",
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
                        imagen = when (datos["TIPO"]?.toString()?.uppercase()) {
                            "COSTA" -> com.example.appturismo.R.drawable.montanita
                            "VOLCÁN" -> com.example.appturismo.R.drawable.cotopaxi
                            else -> com.example.appturismo.R.drawable.quilotoa
                        },
                        imagenUrl = null )
                }

                _destinosApi.value = destinos

                println("DESTINOS API CARGADOS: ${destinos.size}")

            } catch (e: Exception) {

                println("ERROR CARGANDO DESTINOS API: ${e.message}")
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