package com.example.appturismo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class Favorito(

    @PrimaryKey
    val id: Int,

    val nombre: String,

    val provincia: String,

    val categoria: String,

    val imagen: Int

)