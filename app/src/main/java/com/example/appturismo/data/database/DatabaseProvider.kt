package com.example.appturismo.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: FavoritoDatabase? = null

    fun getDatabase(context: Context): FavoritoDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                FavoritoDatabase::class.java,
                "favoritos_db"
            ).build()

            INSTANCE = instance

            instance
        }

    }

}