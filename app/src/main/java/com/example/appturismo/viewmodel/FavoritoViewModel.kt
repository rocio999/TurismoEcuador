package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appturismo.data.database.repository.FavoritoRepository
import com.example.appturismo.data.database.Favorito
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class FavoritoViewModel(
    private val repository: FavoritoRepository

) : ViewModel() {
    val favoritos: SnapshotStateList<Favorito> = mutableStateListOf()
    fun guardarFavorito(favorito: Favorito) {

        viewModelScope.launch {
            repository.guardarFavorito(favorito)
        }

    }
    fun eliminarFavorito(favorito: Favorito) {

        viewModelScope.launch {
            repository.eliminarFavorito(favorito)
        }

    }
    fun cargarFavoritos() {

        viewModelScope.launch {

            favoritos.clear()

            favoritos.addAll(
                repository.obtenerFavoritos()
            )

        }

    }



}
