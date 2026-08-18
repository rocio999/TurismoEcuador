package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appturismo.R
import com.example.appturismo.data.database.model.Destino
import com.example.appturismo.data.database.remote.DestinoApiFeature
import com.example.appturismo.data.database.repository.DestinoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DestinoViewModel : ViewModel() {

    private val _destinosApi = MutableStateFlow<List<Destino>>(emptyList())
    val destinosApi: StateFlow<List<Destino>> =
        _destinosApi.asStateFlow()

    private val _destinosCercanos = MutableStateFlow<List<Destino>>(emptyList())
    val destinosCercanos: StateFlow<List<Destino>> =
        _destinosCercanos.asStateFlow()

    private val _estado = MutableStateFlow<EstadoCarga>(EstadoCarga.Cargando)
    val estado: StateFlow<EstadoCarga> =
        _estado.asStateFlow()

    // ============================================================
    // CARGAR TODOS LOS DESTINOS DESDE ARC GIS
    // ============================================================

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

                println("PASO 1: CARGANDO DESTINOS DE ARC GIS")

                val respuesta =
                    DestinoRepository.obtenerDestinosApi()

                val destinos =
                    respuesta.features.mapIndexed { index, feature ->

                        convertirDestino(
                            index = index,
                            feature = feature
                        )
                    }

                _destinosApi.value = destinos

                _estado.value = EstadoCarga.Exito

                println(
                    "DESTINOS API CARGADOS: ${destinos.size}"
                )

            } catch (e: Exception) {

                println(
                    "ERROR CARGANDO DESTINOS API: ${e.message}"
                )

                _estado.value = EstadoCarga.Error(
                    e.message ?: "Error desconocido"
                )
            }
        }
    }

    // ============================================================
    // BUSCAR DESTINOS CERCANOS CON ARC GIS
    // ============================================================

    fun cargarDestinosCercanos(
        latitud: Double,
        longitud: Double,
        radioKm: Double = 4000.0
    ) {

        viewModelScope.launch {

            _estado.value = EstadoCarga.Cargando

            try {

                println("====================================")
                println("BUSCANDO DESTINOS CERCANOS CON ARC GIS")
                println("LATITUD: $latitud")
                println("LONGITUD: $longitud")
                println("RADIO: $radioKm km")
                println("====================================")

                // ------------------------------------------------
                // CONSULTA A ARC GIS
                // ------------------------------------------------

                val respuesta =
                    DestinoRepository.obtenerDestinosCercanosApi(
                        latitud = latitud,
                        longitud = longitud,
                        radioKm = radioKm
                    )

                val destinos =
                    respuesta.features.mapIndexed { index, feature ->

                        convertirDestino(
                            index = index,
                            feature = feature
                        )
                    }

                // ------------------------------------------------
                // SI ARC GIS ENCUENTRA DESTINOS
                // ------------------------------------------------

                if (destinos.isNotEmpty()) {

                    _destinosCercanos.value = destinos

                    println(
                        "DESTINOS ARC GIS ENCONTRADOS: ${destinos.size}"
                    )

                } else {

                    // --------------------------------------------
                    // RESPALDO LOCAL
                    // --------------------------------------------

                    println(
                        "ARC GIS NO ENCONTRÓ DESTINOS."
                    )

                    println(
                        "USANDO DESTINOS LOCALES COMO RESPALDO."
                    )

                    val destinosLocales =
                        DestinoRepository.obtenerDestinosCercanos(
                            latitud = latitud,
                            longitud = longitud,
                            radioKm = 100.0
                        )

                    _destinosCercanos.value =
                        destinosLocales

                    println(
                        "DESTINOS LOCALES ENCONTRADOS: " +
                                destinosLocales.size
                    )
                }

                _estado.value = EstadoCarga.Exito

            } catch (e: Exception) {

                println(
                    "ERROR ARC GIS: ${e.message}"
                )

                // ------------------------------------------------
                // RESPALDO LOCAL SI FALLA LA API
                // ------------------------------------------------

                try {

                    val destinosLocales =
                        DestinoRepository.obtenerDestinosCercanos(
                            latitud = latitud,
                            longitud = longitud,
                            radioKm = 100.0
                        )

                    _destinosCercanos.value =
                        destinosLocales

                    _estado.value = EstadoCarga.Exito

                    println(
                        "RESPALDO LOCAL: " +
                                "${destinosLocales.size} destinos"
                    )

                } catch (errorLocal: Exception) {

                    _estado.value = EstadoCarga.Error(
                        errorLocal.message
                            ?: "No se pudieron obtener destinos"
                    )
                }
            }
        }
    }

    // ============================================================
    // CONVERTIR ARC GIS → DESTINO
    // ============================================================

    private fun convertirDestino(
        index: Int,
        feature: DestinoApiFeature
    ): Destino {

        val datos = feature.attributes

        val latitud =
            feature.geometry?.y ?: 0.0

        val longitud =
            feature.geometry?.x ?: 0.0

        val nombreDestino =
            datos["NOMBRE"]?.toString()
                ?: "Sin nombre"

        val nombreMayusculas =
            nombreDestino.uppercase()

        // --------------------------------------------------------
        // IMAGEN
        // --------------------------------------------------------

        val imagenDestino = when {

            nombreMayusculas.contains("QUILOTOA") ->
                R.drawable.quilotoa

            nombreMayusculas.contains("COTOPAXI") ->
                R.drawable.cotopaxi

            nombreMayusculas.contains("MONTAÑITA") ||
                    nombreMayusculas.contains("MONTANITA") ->
                R.drawable.montanita

            else ->
                R.drawable.quilotoa
        }

        // --------------------------------------------------------
        // ID
        // --------------------------------------------------------

        val idGenerado =
            datos["OBJECTID"]
                ?.toString()
                ?.toIntOrNull()
                ?: datos["FID"]
                    ?.toString()
                    ?.toIntOrNull()
                ?: (index + 1000)

        return Destino(

            id = idGenerado,

            nombre = nombreDestino,

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

    // ============================================================
    // DESTINOS LOCALES
    // ============================================================

    fun obtenerDestinos(): List<Destino> {

        return DestinoRepository.obtenerDestinos()
    }

    // ============================================================
    // OBTENER DESTINO POR ID
    // ============================================================

    fun obtenerDestino(id: Int): Destino? {

        return _destinosApi.value.find {
            it.id == id
        }
            ?: _destinosCercanos.value.find {
                it.id == id
            }
            ?: DestinoRepository.obtenerDestinoPorId(id)
    }
}

// ================================================================
// ESTADO DE CARGA
// ================================================================

sealed interface EstadoCarga {

    data object Cargando : EstadoCarga

    data object Exito : EstadoCarga

    data class Error(
        val mensaje: String
    ) : EstadoCarga
}