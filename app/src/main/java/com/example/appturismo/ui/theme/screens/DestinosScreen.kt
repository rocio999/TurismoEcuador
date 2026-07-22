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
import com.example.appturismo.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip

data class Destino(
    val nombre: String,
    val descripcion: String,
    val imagen: Int
)

@Composable
fun DestinosScreen(
    navController: NavController
) {

    val destinos = listOf(
        Destino(
            "🏔️ Quilotoa",
            "Laguna de origen volcánico",
            R.drawable.quilotoa
        ),
        Destino(
            "🌋 Cotopaxi",
            "Parque Nacional",
            R.drawable.cotopaxi
        ),
        Destino(
            "🌊 Montañita",
            "Playa del Ecuador",
            R.drawable.montanita
        )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                    onClick = {
                        navController.navigate("detalle/${destino.nombre}")
                    }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    )
                    {
                    Image(
                        painter = painterResource(id = destino.imagen),
                        contentDescription = destino.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    )


                        Text(
                            text = destino.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = destino.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                    }

                }

            }

        }

    }

}