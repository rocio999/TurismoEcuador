package com.example.appturismo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appturismo.data.database.datastore.PreferenciasDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreferenciasViewModel(
    private val preferenciasDataStore: PreferenciasDataStore
) : ViewModel() {

    val modoOscuro: StateFlow<Boolean> =
        preferenciasDataStore.modoOscuro.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun cambiarModoOscuro(activo: Boolean) {

        viewModelScope.launch {

            preferenciasDataStore.guardarModoOscuro(
                activo
            )
        }
    }
}