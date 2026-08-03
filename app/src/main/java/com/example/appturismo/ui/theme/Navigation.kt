package com.example.appturismo.ui.theme


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appturismo.ui.theme.screens.DestinosScreen
import com.example.appturismo.ui.theme.screens.HomeScreen
import com.example.appturismo.ui.theme.screens.DetalleDestinoScreen
import com.example.appturismo.ui.theme.screens.FavoritosScreen
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.data.database.DatabaseProvider
import com.example.appturismo.data.database.repository.FavoritoRepository
import com.example.appturismo.viewmodel.FavoritoViewModel
import com.example.appturismo.viewmodel.FavoritoViewModelFactory


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
        composable("favoritos") {

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

            FavoritosScreen(
                favoritoViewModel = favoritoViewModel
            )
        }

        composable(
            route = "detalle/{id}"
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0

            DetalleDestinoScreen(id)
        }
    }
}