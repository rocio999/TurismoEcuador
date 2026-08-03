package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appturismo.data.database.repository.FavoritoRepository

class FavoritoViewModelFactory(
    private val repository: FavoritoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FavoritoViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return FavoritoViewModel(repository) as T

        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}