package com.example.appturismo.ui.theme


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appturismo.ui.theme.screens.DestinosScreen
import com.example.appturismo.ui.theme.screens.HomeScreen
import com.example.appturismo.ui.theme.screens.DetalleDestinoScreen


@Composable

fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(navController)
        }

        composable("destinos") {
            DestinosScreen(navController)
        }

        composable("detalle/{nombre}") { backStackEntry ->

            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""

            DetalleDestinoScreen(nombre)

        }
    }
}