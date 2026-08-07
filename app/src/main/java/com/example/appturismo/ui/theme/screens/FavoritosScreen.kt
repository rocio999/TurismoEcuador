package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.appturismo.viewmodel.FavoritoViewModel

@Composable
fun FavoritosScreen(
    favoritoViewModel: FavoritoViewModel
) {

    LaunchedEffect(Unit) {
        favoritoViewModel.cargarFavoritos()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "❤️ Mis Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (favoritoViewModel.favoritos.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "No tienes destinos favoritos todavía."
                )
            }

        } else {

            LazyColumn {

                items(favoritoViewModel.favoritos) { favorito ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 8.dp,
                                vertical = 6.dp
                            ),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column {

                            // UNA SOLA IMAGEN
                            Image(
                                painter = painterResource(
                                    id = favorito.imagen
                                ),
                                contentDescription = favorito.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp
                                        )
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            // INFORMACIÓN DEL DESTINO
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = favorito.nombre,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text(
                                    text = "📍 ${favorito.provincia}"
                                )

                                Text(
                                    text = "🏷️ ${favorito.categoria}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}