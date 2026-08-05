package com.example.appturismo.data.database.model

data class Destino(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val descripcioncompleta: String,
    val provincia: String,
    val categoria: String,
    val calificacion: Double,
    val imagen: Int,
    val imagenUrl: String? = null
)