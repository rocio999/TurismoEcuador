package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.viewmodel.DestinoViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.appturismo.ui.theme.components.DestinoCard

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DestinosScreen(
    navController: NavController,
    viewModel: DestinoViewModel
) {
    var textoBusqueda by remember {
        mutableStateOf("")
    }

    val destinosApi by viewModel.destinosApi.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarDestinosApi()
    }

    val destinos = destinosApi




    Column(

        modifier = Modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(

            title = {
                Text("🌎 Guía Turística")
            },

            colors = TopAppBarDefaults.centerAlignedTopAppBarColors()

        )

        Text(

            text = "Destinos Turísticos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = {
                textoBusqueda = it
            },
            label = {
                Text("Buscar destino")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        val destinosFiltrados = destinos.filter {

            it.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    it.descripcion.contains(textoBusqueda, ignoreCase = true)

        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(destinosFiltrados) { destino ->
                DestinoCard(
                    destino = destino,
                    navController = navController
                )
                }

            }

        }

    }

