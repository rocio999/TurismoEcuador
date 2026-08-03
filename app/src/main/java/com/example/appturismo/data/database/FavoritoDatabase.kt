package com.example.appturismo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Favorito::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritoDatabase : RoomDatabase() {

    abstract fun favoritoDao(): FavoritoDao

}