package com.example.appturismo.data.database.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ApiTestViewModel : ViewModel() {

    fun probarApi() {

        viewModelScope.launch {

            try {

                println("INICIANDO PETICION API CHILE")

                val respuesta = RetrofitClient.apiService.obtenerDestinos()

                println("RESPUESTA RECIBIDA")
                println("TOTAL FEATURES: ${respuesta.features.size}")

                if (respuesta.features.isNotEmpty()) {

                    val datos = respuesta.features[0].attributes

                    println("ATRIBUTOS REALES: $datos")
                    println("NOMBRE: ${datos["NOMBRE"]}")
                    println("PROVINCIA: ${datos["PROVINCIA"]}")
                    println("CATEGORIA: ${datos["CATEGORIA"]}")
                }

            } catch (e: Exception) {

                println("ERROR API: ${e.message}")
                e.printStackTrace()

            }
        }
    }
}