package com.example.appturismo.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.appturismo.data.database.DatabaseProvider
import com.example.appturismo.data.database.repository.FavoritoRepository
import com.example.appturismo.ui.theme.screens.AjustesScreen
import com.example.appturismo.ui.theme.screens.DestinosScreen
import com.example.appturismo.ui.theme.screens.DetalleDestinoScreen
import com.example.appturismo.ui.theme.screens.FavoritosScreen
import com.example.appturismo.ui.theme.screens.HomeScreen
import com.example.appturismo.ui.theme.screens.UbicacionScreen
import com.example.appturismo.viewmodel.DestinoViewModel
import com.example.appturismo.viewmodel.FavoritoViewModel
import com.example.appturismo.viewmodel.FavoritoViewModelFactory


@Composable
fun Navigation() {

    // ============================================================
    // CONTROLADOR DE NAVEGACIÓN
    // ============================================================

    val navController = rememberNavController()


    // ============================================================
    // NAV HOST
    // ============================================================

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {


        // ========================================================
        // HOME
        // ========================================================

        composable("home") {

            HomeScreen(
                navController = navController
            )
        }


        // ========================================================
        // DESTINOS
        // ========================================================

        composable("destinos") {

            val destinoViewModel: DestinoViewModel =
                viewModel()

            DestinosScreen(
                navController = navController,
                viewModel = destinoViewModel
            )
        }


        // ========================================================
        // UBICACIÓN
        // ========================================================

        composable("ubicacion") {

            UbicacionScreen()
        }


        // ========================================================
        // FAVORITOS
        // ========================================================

        composable("favoritos") {

            val context = LocalContext.current


            // ----------------------------------------------------
            // BASE DE DATOS
            // ----------------------------------------------------

            val database = remember {

                DatabaseProvider.getDatabase(
                    context
                )
            }


            // ----------------------------------------------------
            // REPOSITORY
            // ----------------------------------------------------

            val repository = remember {

                FavoritoRepository(
                    database.favoritoDao()
                )
            }


            // ----------------------------------------------------
            // VIEWMODEL
            // ----------------------------------------------------

            val favoritoViewModel: FavoritoViewModel =
                viewModel(
                    factory =
                        FavoritoViewModelFactory(
                            repository
                        )
                )


            // ----------------------------------------------------
            // PANTALLA
            // ----------------------------------------------------

            FavoritosScreen(
                favoritoViewModel = favoritoViewModel
            )
        }


        // ========================================================
        // AJUSTES
        // ========================================================

        composable("ajustes") {

            AjustesScreen()
        }


        // ========================================================
        // DETALLE DEL DESTINO
        // ========================================================

        composable(
            route = "detalle/{id}"
        ) { backStackEntry ->


            // ----------------------------------------------------
            // OBTENER ID
            // ----------------------------------------------------

            val id =
                backStackEntry.arguments
                    ?.getString("id")
                    ?.toIntOrNull()
                    ?: 0


            // ----------------------------------------------------
            // RECUPERAR VIEWMODEL DE DESTINOS
            // ----------------------------------------------------

            val destinosBackStackEntry =
                remember(backStackEntry) {

                    navController.getBackStackEntry(
                        "destinos"
                    )
                }


            val destinoViewModel: DestinoViewModel =
                viewModel(
                    viewModelStoreOwner =
                        destinosBackStackEntry
                )


            // ----------------------------------------------------
            // PANTALLA DE DETALLE
            // ----------------------------------------------------

            DetalleDestinoScreen(
                id = id,
                viewModel = destinoViewModel
            )
        }
    }
}