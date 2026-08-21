package com.example.appturismo.ui.theme.screens

import android.Manifest
import android.content.pm.PackageManager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController

import com.example.appturismo.data.database.model.Destino
import com.example.appturismo.data.database.repository.DestinoRepository
import com.example.appturismo.ui.theme.components.DestinoCard
import com.example.appturismo.viewmodel.DestinoViewModel
import com.example.appturismo.viewmodel.EstadoCarga
import com.example.appturismo.viewmodel.UbicacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinosScreen(
    navController: NavController,
    viewModel: DestinoViewModel
) {

    // ============================================================
    // BÚSQUEDA
    // ============================================================

    var textoBusqueda by remember {
        mutableStateOf("")
    }

    var categoriaSeleccionada by remember {
        mutableStateOf("Todos")
    }

    val scrollFiltros = rememberScrollState()

    val context = LocalContext.current

    // ============================================================
    // VIEWMODEL DE UBICACIÓN
    // ============================================================

    val ubicacionViewModel: UbicacionViewModel = viewModel()

    val ubicacion by
    ubicacionViewModel.ubicacion.collectAsState()

    // ============================================================
    // DATOS
    // ============================================================

    val destinosApi by
    viewModel.destinosApi.collectAsState()

    val destinosCercanos by
    viewModel.destinosCercanos.collectAsState()

    val estado by
    viewModel.estado.collectAsState()

    // ============================================================
    // PERMISO DE UBICACIÓN
    // ============================================================

    val permisoUbicacionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->

            val permisoPreciso =
                permisos[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true

            val permisoAproximado =
                permisos[
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ] == true

            if (
                permisoPreciso ||
                permisoAproximado
            ) {

                ubicacionViewModel.obtenerUbicacion(
                    context
                )
            }
        }

    // ============================================================
    // CARGAR DESTINOS
    // ============================================================

    LaunchedEffect(Unit) {

        viewModel.cargarDestinosApi()
    }

    // ============================================================
    // CUANDO OBTENEMOS UBICACIÓN
    // ============================================================

    LaunchedEffect(ubicacion) {

        ubicacion?.let { location ->

            println(
                "UBICACIÓN RECIBIDA: " +
                        "${location.latitude}, " +
                        "${location.longitude}"
            )

            viewModel.cargarDestinosCercanos(
                latitud = location.latitude,
                longitud = location.longitude,
                radioKm = 4000.0
            )
        }
    }

    // ============================================================
    // LISTA BASE
    // ============================================================

    val listaBase: List<Destino> =
        if (destinosCercanos.isNotEmpty()) {
            destinosCercanos
        } else {
            destinosApi
        }

    // ============================================================
    // FILTRAR DESTINOS
    // ============================================================

    val destinosFiltrados =
        listaBase.filter { destino ->

            // ----------------------------------------------------
            // TEXTO DE BÚSQUEDA
            // ----------------------------------------------------

            val texto =
                textoBusqueda.trim()

            val coincideTexto =
                texto.isEmpty() ||
                        destino.nombre.contains(
                            texto,
                            ignoreCase = true
                        ) ||
                        destino.descripcion.contains(
                            texto,
                            ignoreCase = true
                        ) ||
                        destino.provincia.contains(
                            texto,
                            ignoreCase = true
                        ) ||
                        destino.categoria.contains(
                            texto,
                            ignoreCase = true
                        )

            // ----------------------------------------------------
            // NORMALIZAR CATEGORÍA
            // ----------------------------------------------------

            val categoriaDestino =
                destino.categoria
                    .trim()
                    .uppercase()
                    .replace("Á", "A")
                    .replace("É", "E")
                    .replace("Í", "I")
                    .replace("Ó", "O")
                    .replace("Ú", "U")
                    .replace("Ü", "U")

            // ----------------------------------------------------
            // NORMALIZAR NOMBRE
            // ----------------------------------------------------

            val nombreDestino =
                destino.nombre
                    .trim()
                    .uppercase()
                    .replace("Á", "A")
                    .replace("É", "E")
                    .replace("Í", "I")
                    .replace("Ó", "O")
                    .replace("Ú", "U")
                    .replace("Ü", "U")

            // ----------------------------------------------------
            // FILTROS
            // ----------------------------------------------------

            val coincideCategoria =
                when (categoriaSeleccionada) {

                    // --------------------------------------------
                    // TODOS
                    // --------------------------------------------

                    "Todos" -> true

                    // --------------------------------------------
                    // LAGUNA
                    // --------------------------------------------

                    "Laguna" ->

                        categoriaDestino.contains("LAGUNA") ||
                                nombreDestino.contains("LAGUNA")

                    // --------------------------------------------
                    // MONTAÑA
                    // --------------------------------------------

                    "Montaña" ->

                        categoriaDestino.contains("MONTANA") ||
                                categoriaDestino.contains("MONTAÑA") ||
                                categoriaDestino.contains("CORDILLERA") ||
                                categoriaDestino.contains("VOLCAN") ||
                                categoriaDestino.contains("VOLCÁN") ||
                                nombreDestino.contains("MONTANA") ||
                                nombreDestino.contains("MONTAÑA") ||
                                nombreDestino.contains("COTOPAXI")

                    // --------------------------------------------
                    // PLAYA
                    // --------------------------------------------

                    "Playa" ->

                        categoriaDestino.contains("PLAYA") ||
                                categoriaDestino.contains("COSTA") ||
                                categoriaDestino.contains("MAR") ||
                                nombreDestino.contains("PLAYA") ||
                                nombreDestino.contains("MONTANITA")

                    // --------------------------------------------
                    // NATURALEZA
                    // --------------------------------------------

                    "Naturaleza" ->

                        categoriaDestino.contains("NATURALEZA") ||
                                categoriaDestino.contains(
                                    "SITIOS NATURALES"
                                ) ||
                                categoriaDestino.contains(
                                    "SITIO NATURAL"
                                ) ||
                                categoriaDestino.contains(
                                    "AREA NATURAL"
                                ) ||
                                categoriaDestino.contains(
                                    "AREAS NATURALES"
                                ) ||
                                categoriaDestino.contains(
                                    "RESERVA"
                                ) ||
                                categoriaDestino.contains(
                                    "PARQUE NACIONAL"
                                ) ||
                                categoriaDestino.contains(
                                    "BOSQUE"
                                )

                    // --------------------------------------------
                    // CULTURA
                    // --------------------------------------------

                    "Cultura" ->

                        categoriaDestino.contains(
                            "CULTURA"
                        ) ||
                                categoriaDestino.contains(
                                    "MUSEO"
                                ) ||
                                categoriaDestino.contains(
                                    "MUSEOS"
                                ) ||
                                categoriaDestino.contains(
                                    "REALIZACIONES TECNICAS"
                                ) ||
                                categoriaDestino.contains(
                                    "REALIZACION TECNICA"
                                ) ||
                                categoriaDestino.contains(
                                    "HISTORICO"
                                ) ||
                                categoriaDestino.contains(
                                    "ARQUEOLOG"
                                )

                    // --------------------------------------------
                    // FOLKLORE
                    // --------------------------------------------

                    "Folklore" ->

                        categoriaDestino.contains(
                            "FOLKLORE"
                        ) ||
                                categoriaDestino.contains(
                                    "FOLKLOR"
                                ) ||
                                categoriaDestino.contains(
                                    "MANIFESTACION CULTURAL"
                                ) ||
                                categoriaDestino.contains(
                                    "MANIFESTACIONES CULTURALES"
                                ) ||
                                categoriaDestino.contains(
                                    "ETNOGRAF"
                                ) ||
                                categoriaDestino.contains(
                                    "TRADICION"
                                ) ||
                                categoriaDestino.contains(
                                    "TRADICIONES"
                                )

                    // --------------------------------------------
                    // EVENTOS
                    // --------------------------------------------

                    "Eventos" ->

                        categoriaDestino.contains(
                            "EVENTO"
                        ) ||
                                categoriaDestino.contains(
                                    "EVENTOS"
                                ) ||
                                categoriaDestino.contains(
                                    "ACONTECIMIENTO"
                                ) ||
                                categoriaDestino.contains(
                                    "ACONTECIMIENTOS PROGRAMADOS"
                                ) ||
                                categoriaDestino.contains(
                                    "FESTIV"
                                ) ||
                                categoriaDestino.contains(
                                    "FERIA"
                                ) ||
                                categoriaDestino.contains(
                                    "FERIAS"
                                )

                    // --------------------------------------------
                    // RECREACIÓN
                    // --------------------------------------------

                    "Recreación" -> {

                        val tipoDestino =
                            destino.descripcion
                                .trim()
                                .uppercase()
                                .replace("Á", "A")
                                .replace("É", "E")
                                .replace("Í", "I")
                                .replace("Ó", "O")
                                .replace("Ú", "U")

                        categoriaDestino.contains("RECREACION") ||
                                categoriaDestino.contains("ESPARCIMIENTO") ||
                                categoriaDestino.contains("RECREATIVO") ||
                                categoriaDestino.contains("RECREATIVA") ||
                                categoriaDestino.contains("CENTRO O LUGAR DE ESPARCIMIENTO") ||
                                categoriaDestino.contains("CENTRO DE RECREACION") ||
                                categoriaDestino.contains("CENTROS DE RECREACION") ||
                                categoriaDestino.contains("LUGAR DE RECREACION") ||
                                categoriaDestino.contains("LUGARES DE RECREACION") ||
                                categoriaDestino.contains("DEPORT") ||
                                categoriaDestino.contains("PARQUE RECREATIVO") ||
                                categoriaDestino.contains("PARQUE") ||
                                categoriaDestino.contains("BALNEARIO") ||
                                categoriaDestino.contains("COMPLEJO") ||
                                categoriaDestino.contains("PISCINA") ||
                                categoriaDestino.contains("CANCHA") ||
                                categoriaDestino.contains("ESTADIO") ||
                                categoriaDestino.contains("CENTRO DEPORTIVO") ||
                                tipoDestino.contains("RECREACION") ||
                                tipoDestino.contains("ESPARCIMIENTO") ||
                                tipoDestino.contains("RECREATIVO") ||
                                tipoDestino.contains("RECREATIVA") ||
                                tipoDestino.contains("DEPORT")
                    }

                    else -> true
                }

            // ----------------------------------------------------
            // RESULTADO FINAL
            // ----------------------------------------------------

            coincideTexto &&
                    coincideCategoria
        }

    // ============================================================
    // MOSTRAR EN LOG LAS CATEGORÍAS
    // ============================================================

    listaBase.forEach { destino ->

        println(
            "DESTINO: ${destino.nombre} | " +
                    "CATEGORIA: ${destino.categoria}"
        )
    }

    // ============================================================
    // CALCULAR DISTANCIA
    // ============================================================

    val destinosOrdenados =
        remember(
            destinosFiltrados,
            ubicacion
        ) {

            if (ubicacion != null) {

                destinosFiltrados
                    .map { destino ->

                        val distancia =
                            DestinoRepository.calcularDistanciaKm(

                                lat1 =
                                    ubicacion!!.latitude,

                                lon1 =
                                    ubicacion!!.longitude,

                                lat2 =
                                    destino.latitud,

                                lon2 =
                                    destino.longitud
                            )

                        destino to distancia
                    }
                    .sortedBy {
                        it.second
                    }

            } else {

                destinosFiltrados.map { destino ->

                    destino to null
                }
            }
        }

    // ============================================================
    // INTERFAZ
    // ============================================================

    Column(
        modifier =
            Modifier.fillMaxSize()
    ) {

        // ========================================================
        // BARRA SUPERIOR
        // ========================================================

        CenterAlignedTopAppBar(

            title = {
                Text("🌎 Guía Turística")
            }
        )

        // ========================================================
        // TÍTULO
        // ========================================================

        Text(

            text =
                "Destinos Turísticos",

            style =
                MaterialTheme.typography.headlineMedium,

            modifier =
                Modifier.padding(16.dp)
        )

        // ========================================================
        // BUSCADOR
        // ========================================================

        OutlinedTextField(

            value =
                textoBusqueda,

            onValueChange = {
                textoBusqueda = it
            },

            placeholder = {
                Text("Buscar destinos...")
            },

            leadingIcon = {

                Icon(
                    imageVector =
                        Icons.Default.Search,

                    contentDescription =
                        "Buscar"
                )
            },

            trailingIcon = {

                if (
                    textoBusqueda.isNotEmpty()
                ) {

                    IconButton(

                        onClick = {
                            textoBusqueda = ""
                        }

                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Clear,

                            contentDescription =
                                "Limpiar búsqueda"
                        )
                    }
                }
            },

            singleLine = true,

            shape =
                RoundedCornerShape(16.dp),

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    )
        )

        // ========================================================
        // FILTROS
        // ========================================================

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        scrollFiltros
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            val categorias =
                listOf(
                    "Todos",
                    "Laguna",
                    "Montaña",
                    "Playa",
                    "Naturaleza",
                    "Cultura",
                    "Folklore",
                    "Eventos",
                    "Recreación"
                )

            categorias.forEach { categoria ->

                FilterChip(

                    selected =
                        categoriaSeleccionada ==
                                categoria,

                    onClick = {

                        categoriaSeleccionada =
                            categoria
                    },

                    label = {
                        Text(categoria)
                    }
                )
            }
        }

        // ========================================================
        // BOTÓN GPS
        // ========================================================

        Button(

            onClick = {

                val permisoPreciso =
                    ContextCompat.checkSelfPermission(

                        context,

                        Manifest.permission
                            .ACCESS_FINE_LOCATION

                    ) ==
                            PackageManager.PERMISSION_GRANTED

                val permisoAproximado =
                    ContextCompat.checkSelfPermission(

                        context,

                        Manifest.permission
                            .ACCESS_COARSE_LOCATION

                    ) ==
                            PackageManager.PERMISSION_GRANTED

                if (
                    permisoPreciso ||
                    permisoAproximado
                ) {

                    ubicacionViewModel
                        .obtenerUbicacion(
                            context
                        )

                } else {

                    permisoUbicacionLauncher
                        .launch(

                            arrayOf(

                                Manifest.permission
                                    .ACCESS_FINE_LOCATION,

                                Manifest.permission
                                    .ACCESS_COARSE_LOCATION
                            )
                        )
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
        ) {

            Text(
                "📍 Buscar destinos cerca de mí"
            )
        }

        // ========================================================
        // UBICACIÓN
        // ========================================================

        if (ubicacion != null) {

            Text(

                text =
                    "📍 Ubicación detectada",

                style =
                    MaterialTheme.typography.bodyMedium,

                modifier =
                    Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    )
            )
        }

        // ========================================================
        // CONTENIDO
        // ========================================================

        when (
            val estadoActual = estado
        ) {

            // ----------------------------------------------------
            // CARGANDO
            // ----------------------------------------------------

            EstadoCarga.Cargando -> {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Text(

                        text =
                            "Cargando destinos...",

                        modifier =
                            Modifier.padding(
                                top = 8.dp
                            )
                    )
                }
            }

            // ----------------------------------------------------
            // ÉXITO
            // ----------------------------------------------------

            EstadoCarga.Exito -> {

                if (
                    destinosOrdenados.isEmpty()
                ) {

                    Column(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(24.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "📍 No hay destinos que coincidan"
                        )

                        Text(

                            text =
                                "Prueba con otra búsqueda o categoría.",

                            modifier =
                                Modifier.padding(
                                    top = 8.dp
                                )
                        )
                    }

                } else {

                    LazyColumn(

                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(8.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(
                                12.dp
                            )
                    ) {

                        items(

                            items =
                                destinosOrdenados,

                            key = {
                                    (destino, _) ->
                                destino.id
                            }

                        ) { (destino, distancia) ->

                            DestinoCard(

                                destino =
                                    destino,

                                navController =
                                    navController,

                                distanciaKm =
                                    distancia
                            )
                        }
                    }
                }
            }

            // ----------------------------------------------------
            // ERROR
            // ----------------------------------------------------

            is EstadoCarga.Error -> {

                Column(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            "❌ No se pudieron cargar los destinos"
                    )

                    Text(

                        text =
                            estadoActual.mensaje,

                        modifier =
                            Modifier.padding(
                                top = 8.dp
                            )
                    )
                }
            }
        }
    }
}