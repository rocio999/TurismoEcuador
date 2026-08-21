package com.example.appturismo.ui.theme.components

import android.content.Context

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.appturismo.data.database.DatabaseProvider
import com.example.appturismo.data.database.Favorito
import com.example.appturismo.data.database.model.Destino
import com.example.appturismo.data.database.repository.FavoritoRepository
import com.example.appturismo.viewmodel.FavoritoViewModel
import com.example.appturismo.viewmodel.FavoritoViewModelFactory


@Composable
fun DestinoCard(
    destino: Destino,
    navController: NavController,
    distanciaKm: Double? = null
) {

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

    var favorito by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(destino.id) {

        favoritoViewModel.comprobarFavorito(destino.id) { resultado ->
            favorito = resultado
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),

        shape = RoundedCornerShape(20.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

        onClick = {
            navController.navigate("detalle/${destino.id}")
        }
    ) {

        Column {

            // IMAGEN


            Image(
                painter = painterResource(
                    id = destino.imagen
                ),

                contentDescription = destino.nombre,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    ),

                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {


                // NOMBRE + FAVORITO


                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = destino.nombre,

                        style =
                            MaterialTheme.typography.titleLarge,

                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {

                            favorito = !favorito

                            val favoritoDestino =
                                Favorito(
                                    id = destino.id,
                                    nombre = destino.nombre,
                                    provincia = destino.provincia,
                                    categoria = destino.categoria,
                                    imagen = destino.imagen
                                )

                            if (favorito) {

                                favoritoViewModel
                                    .guardarFavorito(
                                        favoritoDestino
                                    )

                            } else {

                                favoritoViewModel
                                    .eliminarFavorito(
                                        favoritoDestino
                                    )
                            }
                        }
                    ) {

                        Icon(

                            imageVector =
                                if (favorito)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,

                            contentDescription =
                                "Favorito",

                            tint =
                                if (favorito)
                                    Color.Red
                                else
                                    Color.Gray
                        )
                    }
                }


// INFORMACIÓN


                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    // PROVINCIA
                    Text(
                        text = "📍 ${destino.provincia}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    // CATEGORÍA
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Text(
                            text = "🏷️ ${destino.categoria}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            )
                        )
                    }

                    // CALIFICACIÓN
                    if (destino.calificacion > 0) {

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {

                            Text(
                                text = "⭐ ${destino.calificacion}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 5.dp
                                )
                            )
                        }
                    }

                    // DISTANCIA
                    if (distanciaKm != null) {

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {

                            Text(
                                text = if (distanciaKm < 1.0) {
                                    "📍 A menos de 1 km de ti"
                                } else {
                                    "📍 ${"%.1f".format(distanciaKm)} km de ti"
                                },

                                style = MaterialTheme.typography.bodyMedium,

                                color = MaterialTheme.colorScheme.onPrimaryContainer,

                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // DESCRIPCIÓN
                    Text(
                        text = destino.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}