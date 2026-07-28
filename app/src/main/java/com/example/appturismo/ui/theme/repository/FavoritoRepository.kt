package com.example.appturismo.ui.theme.repository

import com.example.appturismo.ui.theme.database.FavoritoDao
import com.example.appturismo.ui.theme.database.Favorito


class FavoritoRepository(
    private val favoritoDao: FavoritoDao
){
    suspend fun guardarFavorito(
        favorito: Favorito
    ) {
        favoritoDao.insertarFavorito(favorito)
    }
}