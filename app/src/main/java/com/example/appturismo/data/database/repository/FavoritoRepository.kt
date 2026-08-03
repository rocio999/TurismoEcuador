package com.example.appturismo.data.database.repository

import com.example.appturismo.data.database.FavoritoDao
import com.example.appturismo.data.database.Favorito


class FavoritoRepository(
    private val favoritoDao: FavoritoDao
){
    suspend fun guardarFavorito(
        favorito: Favorito
    ) {
        favoritoDao.insertarFavorito(favorito)
    }
    suspend fun obtenerFavoritos(): List<Favorito> {
        return favoritoDao.obtenerFavoritos()
    }
    suspend fun esFavorito(id: Int): Boolean {
        return favoritoDao.esFavorito(id)
    }
    suspend fun eliminarFavorito(
        favorito: Favorito
    ) {
        favoritoDao.eliminarFavorito(favorito)
    }

}