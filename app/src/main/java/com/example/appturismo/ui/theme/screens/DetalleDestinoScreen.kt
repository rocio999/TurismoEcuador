
package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.appturismo.data.database.DatabaseProvider
import com.example.appturismo.data.database.Favorito
import com.example.appturismo.data.database.repository.FavoritoRepository
import com.example.appturismo.viewmodel.DestinoViewModel
import com.example.appturismo.viewmodel.FavoritoViewModel
import com.example.appturismo.viewmodel.FavoritoViewModelFactory

@Composable
fun DetalleDestinoScreen(
    id: Int,
    viewModel: DestinoViewModel
) {

    val destino = viewModel.obtenerDestino(id)

    if (destino == null) {
        Text("Destino no encontrado")
        return
    }

    val context = LocalContext.current

    val database = remember {
        DatabaseProvider.getDatabase(context)
    }

    val repository = remember {
        FavoritoRepository(database.favoritoDao())
    }

    val favoritoViewModel: FavoritoViewModel = viewModel(
        factory = FavoritoViewModelFactory(repository)
    )

    var esFavorito by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(destino.id) {

        favoritoViewModel.comprobarFavorito(destino.id) { resultado ->
            esFavorito = resultado
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // IMAGEN LOCAL
        Image(
            painter = painterResource(
                id = destino.imagen
            ),
            contentDescription = destino.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                ),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = destino.nombre,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            "📍 Provincia: ${destino.provincia}"
        )

        Text(
            "🏷️ Categoría: ${destino.categoria}"
        )

        Text(
            "⭐ ${destino.calificacion}"
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = destino.descripcioncompleta,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {

                val favoritoDestino = Favorito(
                    id = destino.id,
                    nombre = destino.nombre,
                    provincia = destino.provincia,
                    categoria = destino.categoria,
                    imagen = destino.imagen
                )

                if (esFavorito) {

                    favoritoViewModel.eliminarFavorito(
                        favoritoDestino
                    )

                    esFavorito = false

                } else {

                    favoritoViewModel.guardarFavorito(
                        favoritoDestino
                    )

                    esFavorito = true
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (esFavorito)
                    "💔 Quitar de favoritos"
                else
                    "❤️ Agregar a favoritos"
            )
        }
    }
}