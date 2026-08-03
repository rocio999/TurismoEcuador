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

        LazyColumn {

            items(favoritoViewModel.favoritos) { favorito ->

                Text(
                    text = favorito.nombre,
                    modifier = Modifier.padding(8.dp)
                )

            }

        }

    }

}