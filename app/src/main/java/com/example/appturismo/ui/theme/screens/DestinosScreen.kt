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

data class Destino(
    val nombre: String,
    val descripcion: String
)

@Composable
fun DestinosScreen(
    navController: NavController
) {

    val destinos = listOf(
        Destino("🏔️ Quilotoa", "Laguna de origen volcánico"),
        Destino("🌋 Cotopaxi", "Parque Nacional"),
        Destino("🌊 Montañita", "Playa del Ecuador")
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Destinos Turísticos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(destinos) { destino ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("detalle/${destino.nombre}")
                    }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = destino.nombre,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = destino.descripcion
                        )

                    }

                }

            }

        }

    }

}