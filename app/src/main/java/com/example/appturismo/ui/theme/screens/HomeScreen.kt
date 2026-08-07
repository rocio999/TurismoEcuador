package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appturismo.R

@Composable
fun HomeScreen(navController: NavHostController) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ==============================
        // PORTADA
        // ==============================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp
                    )
                )
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.portadas_turismos
                ),
                contentDescription = "Paisaje turístico",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Degradado para que el texto sea visible
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.75f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {

                Text(
                    text = "Destinos",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Explora. Descubre. Vive.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Tu próxima aventura comienza aquí.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        // ==============================
        // CONTENIDO
        // ==============================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
        ) {

            Text(
                text = "Descubre nuevos lugares",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Explora destinos increíbles de Sudamérica.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // ==============================
            // EXPLORAR
            // ==============================

            HomeOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = null
                    )
                },
                title = "Explorar destinos",
                description = "Descubre lugares increíbles",
                onClick = {
                    navController.navigate("destinos")
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // ==============================
            // FAVORITOS
            // ==============================

            HomeOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                },
                title = "Mis favoritos",
                description = "Consulta tus destinos guardados",
                onClick = {
                    navController.navigate("favoritos")
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // ==============================
            // UBICACIÓN
            // ==============================

            HomeOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                },
                title = "Mi ubicación",
                description = "Consulta tu posición actual",
                onClick = {
                    navController.navigate("ubicacion")
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // ==============================
            // AJUSTES
            // ==============================

            HomeOptionCard(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                },
                title = "Ajustes",
                description = "Personaliza tu experiencia",
                onClick = {
                    navController.navigate("ajustes")
                }
            )
        }
    }


}

@Composable
private fun HomeOptionCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit
) {


    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {

                icon()
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir"
            )
        }
    }


}
