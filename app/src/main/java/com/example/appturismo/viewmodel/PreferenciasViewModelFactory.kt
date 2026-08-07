package com.example.appturismo.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appturismo.data.database.datastore.PreferenciasDataStore

class PreferenciasViewModelFactory(
    private val preferenciasDataStore: PreferenciasDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                PreferenciasViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return PreferenciasViewModel(
                preferenciasDataStore
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}