package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.ui.theme.viewmodel.DestinoViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import com.example.appturismo.ui.theme.components.DestinoCard
import com.example.appturismo.ui.theme.repository.DestinoRepository

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DestinosScreen(
    navController: NavController
) {
    var textoBusqueda by remember {
        mutableStateOf("")
    }

    val viewModel: DestinoViewModel = viewModel()

    val destinos = viewModel.obtenerDestinos()




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

