package com.example.appturismo.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFavorito(favorito: Favorito)

    @Delete
    suspend fun eliminarFavorito(favorito: Favorito)

    @Query("SELECT * FROM favoritos")
    suspend fun obtenerFavoritos(): List<Favorito>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE id = :id)")
    suspend fun esFavorito(id: Int): Boolean

}