package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.data.database.remote.ApiTestViewModel



@Composable
fun HomeScreen(navController: NavHostController) {
    val apiTestViewModel: ApiTestViewModel = viewModel()

     LaunchedEffect(Unit) {
    apiTestViewModel.probarApi()
}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🌎 Destinos Turísticos✈\uFE0F ",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Ecuador \uD83C\uDDEA\uD83C\uDDE8 ",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Explora los lugares más increíbles del Ecuador."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("destinos")
            }
        ){
            Text("Explorar")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("favoritos")
            }
        ) {
            Text("❤️ Mis Favoritos")
        }

    }
}