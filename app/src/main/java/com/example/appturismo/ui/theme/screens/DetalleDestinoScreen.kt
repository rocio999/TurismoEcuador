package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.viewmodel.DestinoViewModel
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button


@Composable
fun DetalleDestinoScreen(
    id: Int
) {

    val viewModel: DestinoViewModel = viewModel()

    val destino = viewModel.obtenerDestino(id)

    if (destino == null) {

        Text("Destino no encontrado")

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
    Image(
            painter = painterResource(id = destino.imagen),
    contentDescription = destino.nombre,
    modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)
        .clip(RoundedCornerShape(20.dp)),
    contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.height(20.dp))


        Text(
            text = destino.nombre,
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(modifier = Modifier.height(12.dp))

        Text("📍 Provincia: ${destino.provincia}")

        Text("🏷️ Categoría: ${destino.categoria}")

        Text("⭐ ${destino.calificacion}")

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = destino.descripcioncompleta,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("❤️ Agregar a favoritos")
        }
}}