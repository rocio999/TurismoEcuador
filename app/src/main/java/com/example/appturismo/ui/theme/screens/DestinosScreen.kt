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
import androidx.compose.material3.TopAppBarDefaults

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
    // ESTADOS DE BÚSQUEDA Y FILTRO
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

    val ubicacion by ubicacionViewModel.ubicacion.collectAsState()

    // ============================================================
    // DATOS DE DESTINOS
    // ============================================================

    val destinosApi by viewModel.destinosApi.collectAsState()

    val destinosCercanos by viewModel.destinosCercanos.collectAsState()

    val estado by viewModel.estado.collectAsState()

    // ============================================================
    // PERMISO DE UBICACIÓN
    // ============================================================

    val permisoUbicacionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->

            val permisoPreciso =
                permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true

            val permisoAproximado =
                permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (permisoPreciso || permisoAproximado) {

                ubicacionViewModel.obtenerUbicacion(context)

            } else {

                println("PERMISO GPS DENEGADO")
            }
        }

    // ============================================================
    // CARGAR DESTINOS DE ARC GIS
    // ============================================================

    LaunchedEffect(Unit) {

        viewModel.cargarDestinosApi()
    }

    // ============================================================
    // CUANDO SE OBTIENE LA UBICACIÓN
    // ============================================================

    LaunchedEffect(ubicacion) {

        ubicacion?.let { location ->

            println(
                "UBICACIÓN RECIBIDA: " +
                        "${location.latitude}, ${location.longitude}"
            )

            viewModel.cargarDestinosCercanos(
                latitud = location.latitude,
                longitud = location.longitude,
                radioKm = 4000.0
            )
        }
    }

    // ============================================================
    // DESTINOS A MOSTRAR
    // ============================================================

    val listaBase: List<Destino> =
        if (destinosCercanos.isNotEmpty()) {
            destinosCercanos
        } else {
            destinosApi
        }
    listaBase.forEach { destino ->
        println(
            "CATEGORIA DESTINO: ${destino.nombre} -> ${destino.categoria}"
        )
    }

    // ============================================================
    // FILTRAR POR TEXTO Y CATEGORÍA
    // ============================================================

    val destinosFiltrados = listaBase.filter { destino ->

        val coincideTexto =
            destino.nombre.contains(
                textoBusqueda,
                ignoreCase = true
            ) ||
                    destino.descripcion.contains(
                        textoBusqueda,
                        ignoreCase = true
                    )

        val coincideCategoria = when (categoriaSeleccionada) {

            "Todos" -> true

            "Naturaleza" ->
                destino.categoria.contains(
                    "SITIOS NATURALES",
                    ignoreCase = true
                )

            "Cultura" ->
                destino.categoria.contains(
                    "MUSEOS",
                    ignoreCase = true
                ) ||
                        destino.categoria.contains(
                            "REALIZACIONES TÉCNICAS",
                            ignoreCase = true
                        )

            "Eventos" ->
                destino.categoria.contains(
                    "ACONTECIMIENTOS PROGRAMADOS",
                    ignoreCase = true
                )

            "Folklore" ->
                destino.categoria.contains(
                    "FOLKLORE",
                    ignoreCase = true
                )

            "Recreación" ->
                destino.categoria.contains(
                    "CENTRO O LUGAR DE ESPARCIMIENTO",
                    ignoreCase = true
                )

            else -> true
        }

        coincideTexto && coincideCategoria
    }

    // ============================================================
    // CALCULAR DISTANCIA Y ORDENAR
    // ============================================================

    val destinosOrdenados = remember(
        destinosFiltrados,
        ubicacion
    ) {

        if (ubicacion != null) {

            destinosFiltrados
                .map { destino ->

                    val distancia =
                        DestinoRepository.calcularDistanciaKm(
                            lat1 = ubicacion!!.latitude,
                            lon1 = ubicacion!!.longitude,
                            lat2 = destino.latitud,
                            lon2 = destino.longitud
                        )

                    destino to distancia
                }
                .sortedBy { (_, distancia) ->
                    distancia
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
        modifier = Modifier.fillMaxSize()
    ) {

        // ========================================================
        // BARRA SUPERIOR
        // ========================================================

        CenterAlignedTopAppBar(

            title = {
                Text("🌎 Guía Turística")
            },

            colors =
                TopAppBarDefaults.centerAlignedTopAppBarColors()
        )

        // ========================================================
        // TÍTULO
        // ========================================================

        Text(
            text = "Destinos Turísticos",

            style =
                MaterialTheme.typography.headlineMedium,

            modifier = Modifier.padding(16.dp)
        )

        // ========================================================
        // BUSCADOR
        // ========================================================

        OutlinedTextField(

            value = textoBusqueda,

            onValueChange = {
                textoBusqueda = it
            },

            placeholder = {
                Text("Buscar destinos...")
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },

            trailingIcon = {

                if (textoBusqueda.isNotEmpty()) {

                    IconButton(
                        onClick = {
                            textoBusqueda = ""
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar búsqueda"
                        )
                    }
                }
            },

            singleLine = true,

            shape = RoundedCornerShape(16.dp),

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // ========================================================
        // FILTROS DE CATEGORÍA
        // ========================================================



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollFiltros)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            val categorias = listOf(
                "Todos",
                "Naturaleza",
                "Cultura",
                "Eventos",
                "Folklore",
                "Recreación"
            )

            categorias.forEach { categoria ->

                FilterChip(

                    selected =
                        categoriaSeleccionada == categoria,

                    onClick = {
                        categoriaSeleccionada = categoria
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
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                val permisoAproximado =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                if (permisoPreciso || permisoAproximado) {

                    ubicacionViewModel.obtenerUbicacion(context)

                } else {

                    permisoUbicacionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {

            Text("📍 Buscar destinos cerca de mí")
        }

        // ========================================================
        // UBICACIÓN DETECTADA
        // ========================================================

        if (ubicacion != null) {

            Text(
                text = "📍 Ubicación detectada",

                style =
                    MaterialTheme.typography.bodyMedium,

                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                )
            )
        }

        // ========================================================
        // CONTENIDO
        // ========================================================

        when (val estadoActual = estado) {

            EstadoCarga.Cargando -> {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Text(
                        text = "Cargando destinos...",

                        modifier =
                            Modifier.padding(top = 8.dp)
                    )
                }
            }

            EstadoCarga.Exito -> {

                if (destinosOrdenados.isEmpty()) {

                    Column(
                        modifier = Modifier
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
                                Modifier.padding(top = 8.dp)
                        )
                    }

                } else {

                    LazyColumn(

                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(8.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)

                    ) {

                        items(
                            items = destinosOrdenados,

                            key = { (destino, _) ->
                                destino.id
                            }

                        ) { (destino, distancia) ->

                            DestinoCard(

                                destino = destino,

                                navController =
                                    navController,

                                distanciaKm =
                                    distancia
                            )
                        }
                    }
                }
            }

            is EstadoCarga.Error -> {

                Column(
                    modifier = Modifier
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
                            Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}