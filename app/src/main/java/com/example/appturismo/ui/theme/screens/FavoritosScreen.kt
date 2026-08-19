package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.appturismo.R
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
                    text = "❤️"
                )

                Text(
                    text = "No tienes destinos favoritos todavía.",
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Agrega destinos usando el corazón ❤️",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),

                contentPadding =
                    androidx.compose.foundation.layout.PaddingValues(
                        8.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = favoritoViewModel.favoritos,
                    key = { favorito -> favorito.id }
                ) { favorito ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column {

                            // =================================================
                            // IMAGEN
                            // =================================================

                            Image(
                                painter = painterResource(
                                    id = obtenerImagenValida(
                                        favorito.imagen
                                    )
                                ),

                                contentDescription =
                                    favorito.nombre,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp
                                        )
                                    ),

                                contentScale =
                                    ContentScale.Crop
                            )

                            // =================================================
                            // INFORMACIÓN
                            // =================================================

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = favorito.nombre,
                                        style =
                                            MaterialTheme
                                                .typography
                                                .titleLarge
                                    )

                                    Text(
                                        text =
                                            "📍 ${favorito.provincia}",
                                        modifier =
                                            Modifier.padding(top = 4.dp)
                                    )

                                    Text(
                                        text =
                                            "🏷️ ${favorito.categoria}",
                                        modifier =
                                            Modifier.padding(top = 4.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {

                                        favoritoViewModel
                                            .eliminarFavorito(
                                                favorito
                                            )
                                    }
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Default.Delete,

                                        contentDescription =
                                            "Eliminar favorito",

                                        tint =
                                            MaterialTheme
                                                .colorScheme
                                                .error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// ================================================================
// IMAGEN SEGURA
// ================================================================

private fun obtenerImagenValida(
    imagen: Int
): Int {

    return if (imagen != 0) {
        imagen
    } else {
        R.drawable.naturaleza
    }
}