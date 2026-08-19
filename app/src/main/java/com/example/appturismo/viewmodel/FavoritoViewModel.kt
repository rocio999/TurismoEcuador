package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appturismo.data.database.Favorito
import com.example.appturismo.data.database.repository.FavoritoRepository
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.launch

class FavoritoViewModel(
    private val repository: FavoritoRepository
) : ViewModel() {

    // ============================================================
    // LISTA DE FAVORITOS
    // ============================================================

    val favoritos: SnapshotStateList<Favorito> =
        mutableStateListOf()


    // ============================================================
    // CARGAR FAVORITOS
    // ============================================================

    fun cargarFavoritos() {

        viewModelScope.launch {

            try {

                val lista =
                    repository.obtenerFavoritos()

                favoritos.clear()

                favoritos.addAll(lista)

                println(
                    "FAVORITOS CARGADOS: ${favoritos.size}"
                )

            } catch (e: Exception) {

                println(
                    "ERROR CARGANDO FAVORITOS: ${e.message}"
                )
            }
        }
    }


    // ============================================================
    // GUARDAR FAVORITO
    // ============================================================

    fun guardarFavorito(
        favorito: Favorito
    ) {

        viewModelScope.launch {

            try {

                repository.guardarFavorito(favorito)

                // Actualizamos la lista inmediatamente
                val existe =
                    favoritos.any {
                        it.id == favorito.id
                    }

                if (!existe) {

                    favoritos.add(favorito)

                }

                println(
                    "FAVORITO GUARDADO: ${favorito.nombre}"
                )

            } catch (e: Exception) {

                println(
                    "ERROR GUARDANDO FAVORITO: ${e.message}"
                )
            }
        }
    }


    // ============================================================
    // ELIMINAR FAVORITO
    // ============================================================

    fun eliminarFavorito(
        favorito: Favorito
    ) {

        viewModelScope.launch {

            try {

                repository.eliminarFavorito(favorito)

                favoritos.removeAll {
                    it.id == favorito.id
                }

                println(
                    "FAVORITO ELIMINADO: ${favorito.nombre}"
                )

            } catch (e: Exception) {

                println(
                    "ERROR ELIMINANDO FAVORITO: ${e.message}"
                )
            }
        }
    }


    // ============================================================
    // COMPROBAR SI ES FAVORITO
    // ============================================================

    fun comprobarFavorito(
        id: Int,
        onResult: (Boolean) -> Unit
    ) {

        viewModelScope.launch {

            try {

                val resultado =
                    repository.esFavorito(id)

                onResult(resultado)

            } catch (e: Exception) {

                println(
                    "ERROR COMPROBANDO FAVORITO: ${e.message}"
                )

                onResult(false)
            }
        }
    }
}