package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.appturismo.viewmodel.FavoritoViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card

@Composable
fun FavoritosScreen(
    favoritoViewModel: FavoritoViewModel
) {
    LaunchedEffect(Unit) {
        favoritoViewModel.cargarFavoritos()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "❤️ Mis Favoritos",
            style = MaterialTheme.typography.headlineMedium
        )

        if (favoritoViewModel.favoritos.isEmpty()) {

            Text(
                text = "No tienes destinos favoritos todavía."
            )

        } else {

            LazyColumn {

                items(favoritoViewModel.favoritos) { favorito ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

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