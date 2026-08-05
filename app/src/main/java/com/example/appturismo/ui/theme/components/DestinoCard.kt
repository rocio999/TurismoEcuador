package com.example.appturismo.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appturismo.data.database.model.Destino
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.data.database.Favorito
import com.example.appturismo.viewmodel.FavoritoViewModel
import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.appturismo.data.database.DatabaseProvider
import com.example.appturismo.data.database.repository.FavoritoRepository
import com.example.appturismo.viewmodel.FavoritoViewModelFactory

@Composable
fun DestinoCard(
    destino: Destino,
    navController: NavController
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
            navController.navigate("detalle/${destino.id}")        }
    ) {

        Column {

            Image(
                painter = painterResource(id = destino.imagen),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = destino.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            favorito = !favorito
                            val favoritoDestino = Favorito(
                                id = destino.id,
                                nombre = destino.nombre,
                                provincia = destino.provincia,
                                categoria = destino.categoria,
                                imagen = destino.imagen
                            )

                            if (favorito) {
                                favoritoViewModel.guardarFavorito(favoritoDestino)
                            } else {
                                favoritoViewModel.eliminarFavorito(favoritoDestino)
                            }
                        }
                    ) {

                        Icon(
                            imageVector = if (favorito)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (favorito) Color.Red else Color.Gray
                        )

                    }

                }

                Text("📍 Provincia: ${destino.provincia}")

                Text("🏷️ Categoría: ${destino.categoria}")

                Text("⭐ ${destino.calificacion}")

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = destino.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )

            }

        }

    }

}