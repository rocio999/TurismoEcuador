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


    // DESTINOS DE ARC GIS


    private val _destinosApi =
        MutableStateFlow<List<Destino>>(emptyList())

    val destinosApi: StateFlow<List<Destino>> =
        _destinosApi.asStateFlow()



    // DESTINOS CERCANOS


    private val _destinosCercanos =
        MutableStateFlow<List<Destino>>(emptyList())

    val destinosCercanos: StateFlow<List<Destino>> =
        _destinosCercanos.asStateFlow()



    // ESTADO


    private val _estado =
        MutableStateFlow<EstadoCarga>(
            EstadoCarga.Cargando
        )

    val estado: StateFlow<EstadoCarga> =
        _estado.asStateFlow()



    // CARGAR TODOS LOS DESTINOS


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

                _estado.value =
                    EstadoCarga.Error(
                        e.message ?: "Error desconocido"
                    )
            }
        }
    }



    // DESTINOS CERCANOS


    fun cargarDestinosCercanos(
        latitud: Double,
        longitud: Double,
        radioKm: Double = 4000.0
    ) {

        viewModelScope.launch {

            _estado.value = EstadoCarga.Cargando

            try {

                println("====================================")
                println("BUSCANDO DESTINOS CERCANOS")
                println("LATITUD: $latitud")
                println("LONGITUD: $longitud")
                println("RADIO: $radioKm km")
                println("====================================")

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

                if (destinos.isNotEmpty()) {

                    _destinosCercanos.value = destinos

                    println(
                        "DESTINOS ARC GIS ENCONTRADOS: ${destinos.size}"
                    )

                } else {

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
                        "DESTINOS LOCALES: ${destinosLocales.size}"
                    )
                }

                _estado.value = EstadoCarga.Exito

            } catch (e: Exception) {

                println(
                    "ERROR ARC GIS: ${e.message}"
                )

                try {

                    val destinosLocales =
                        DestinoRepository.obtenerDestinosCercanos(
                            latitud = latitud,
                            longitud = longitud,
                            radioKm = 100.0
                        )

                    _destinosCercanos.value =
                        destinosLocales

                    _estado.value =
                        EstadoCarga.Exito

                    println(
                        "RESPALDO LOCAL: ${destinosLocales.size} destinos"
                    )

                } catch (errorLocal: Exception) {

                    _estado.value =
                        EstadoCarga.Error(
                            errorLocal.message
                                ?: "No se pudieron obtener destinos"
                        )
                }
            }
        }
    }



    // CONVERTIR ARC GIS → DESTINO


    private fun convertirDestino(
        index: Int,
        feature: DestinoApiFeature
    ): Destino {

        val datos = feature.attributes



        // UBICACIÓN

        val latitud =
            feature.geometry?.y ?: 0.0

        val longitud =
            feature.geometry?.x ?: 0.0


        // NOMBRE


        val nombreDestino =
            datos["NOMBRE"]
                ?.toString()
                ?.trim()
                ?.ifEmpty {
                    "Sin nombre"
                }
                ?: "Sin nombre"



        // CATEGORÍA ORIGINAL


        val categoriaOriginal =
            datos["CATEGORIA"]
                ?.toString()
                ?.trim()
                ?.ifEmpty {
                    "Sin categoría"
                }
                ?: "Sin categoría"



        // DESCRIPCIÓN


        val descripcion =
            datos["TIPO"]
                ?.toString()
                ?.trim()
                ?.ifEmpty {
                    "Destino turístico"
                }
                ?: "Destino turístico"


        val descripcionCompleta =
            datos["DESCRIPCIO"]
                ?.toString()
                ?.trim()
                ?.ifEmpty {
                    "Sin descripción disponible"
                }
                ?: "Sin descripción disponible"


        // NORMALIZAR TODO EL TEXTO


        val textoDestino =
            normalizarTexto(
                nombreDestino + " " +
                        categoriaOriginal + " " +
                        descripcion + " " +
                        descripcionCompleta
            )


        val categoriaNormalizada =
            normalizarTexto(
                categoriaOriginal
            )


        // DETERMINAR CATEGORÍA REAL

        val categoriaFinal = when {

            // LAGUNA

            textoDestino.contains("LAGUNA") ||
                    textoDestino.contains("LAGO") -> {

                "Laguna"
            }


            // PLAYA


            textoDestino.contains("PLAYA") ||
                    textoDestino.contains("BALNEARIO") -> {

                "Playa"
            }


            // MONTAÑA

            textoDestino.contains("MONTANA") ||
                    textoDestino.contains("VOLCAN") ||
                    textoDestino.contains("CERRO") ||
                    textoDestino.contains("NEVADO") -> {

                "Montaña"
            }


            // FOLKLORE

            categoriaNormalizada.contains("FOLKLORE") ||
                    categoriaNormalizada.contains("FOLCLOR") ||
                    textoDestino.contains("FOLKLORE") ||
                    textoDestino.contains("FOLCLOR") -> {

                "Folklore"
            }


            // EVENTOS

            categoriaNormalizada.contains("EVENTO") ||
                    categoriaNormalizada.contains("ACONTECIMIENTO") ||
                    categoriaNormalizada.contains(
                        "ACONTECIMIENTOS PROGRAMADOS"
                    ) ||
                    textoDestino.contains("EVENTO") ||
                    textoDestino.contains("ACONTECIMIENTO") ||
                    textoDestino.contains(
                        "ACONTECIMIENTOS PROGRAMADOS"
                    ) -> {

                "Eventos"
            }


            // RECREACIÓN

            categoriaNormalizada.contains("RECREACION") ||
                    categoriaNormalizada.contains("ESPARCIMIENTO") ||
                    categoriaNormalizada.contains(
                        "CENTRO O LUGAR DE ESPARCIMIENTO"
                    ) ||
                    categoriaNormalizada.contains(
                        "CENTRO DE ESPARCIMIENTO"
                    ) ||
                    textoDestino.contains("RECREACION") ||
                    textoDestino.contains("ESPARCIMIENTO") ||
                    textoDestino.contains(
                        "CENTRO O LUGAR DE ESPARCIMIENTO"
                    ) -> {

                "Recreación"
            }


            // CULTURA

            categoriaNormalizada.contains("CULTURA") ||
                    categoriaNormalizada.contains("MUSEO") ||
                    categoriaNormalizada.contains(
                        "REALIZACIONES TECNICAS"
                    ) ||
                    textoDestino.contains("MUSEO") ||
                    textoDestino.contains("CULTURA") -> {

                "Cultura"
            }


            // NATURALEZA

            categoriaNormalizada.contains("NATURALEZA") ||
                    categoriaNormalizada.contains(
                        "SITIOS NATURALES"
                    ) ||
                    textoDestino.contains("NATURALEZA") -> {

                "Naturaleza"
            }

            // POR DEFECTO

            else -> {

                "Naturaleza"
            }
        }

        // IMAGEN SEGÚN CATEGORÍA


        val imagenDestino = when (categoriaFinal) {

            "Laguna" ->
                R.drawable.laguna

            "Montaña" ->
                R.drawable.montana

            "Playa" ->
                R.drawable.playa

            "Naturaleza" ->
                R.drawable.naturaleza

            "Cultura" ->
                R.drawable.cultura

            "Folklore" ->
                R.drawable.folklore

            "Eventos" ->
                R.drawable.evento

            "Recreación" ->
                R.drawable.recreacion

            else ->
                R.drawable.naturaleza
        }



        // LOG


        println("====================================")
        println("DESTINO: $nombreDestino")
        println("CATEGORÍA ORIGINAL: $categoriaOriginal")
        println("CATEGORÍA FINAL: $categoriaFinal")
        println("IMAGEN: $imagenDestino")
        println("====================================")



        // ID


        val idGenerado =
            datos["OBJECTID"]
                ?.toString()
                ?.toIntOrNull()
                ?: datos["FID"]
                    ?.toString()
                    ?.toIntOrNull()
                ?: (index + 1000)



        // PROVINCIA


        val provinciaDestino =
            datos["PROVINCIA"]
                ?.toString()
                ?.trim()
                ?.ifEmpty {
                    "Sin provincia"
                }
                ?: "Sin provincia"



        // CREAR DESTINO


        return Destino(

            id = idGenerado,

            nombre = nombreDestino,

            descripcion = descripcion,

            descripcioncompleta =
                descripcionCompleta,

            provincia =
                provinciaDestino,

            categoria =
                categoriaFinal,

            calificacion = 0.0,

            imagen =
                imagenDestino,

            imagenUrl = null,

            latitud =
                latitud,

            longitud =
                longitud
        )
    }



    // FUNCIÓN PARA NORMALIZAR TEXTO


    private fun normalizarTexto(
        texto: String
    ): String {

        return texto
            .trim()
            .uppercase()
            .replace("Á", "A")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ó", "O")
            .replace("Ú", "U")
            .replace("Ü", "U")
    }



    // DESTINOS LOCALES


    fun obtenerDestinos(): List<Destino> {

        return DestinoRepository.obtenerDestinos()
    }



    // OBTENER DESTINO POR ID


    fun obtenerDestino(
        id: Int
    ): Destino? {

        return _destinosApi.value.find {
            it.id == id
        }
            ?: _destinosCercanos.value.find {
                it.id == id
            }
            ?: DestinoRepository.obtenerDestinoPorId(
                id
            )
    }
}

// ESTADO DE CARGA


sealed interface EstadoCarga {

    data object Cargando : EstadoCarga

    data object Exito : EstadoCarga

    data class Error(
        val mensaje: String
    ) : EstadoCarga
}