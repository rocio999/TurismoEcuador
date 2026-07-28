package com.example.appturismo.ui.theme.viewmodel


import androidx.lifecycle.ViewModel
import com.example.appturismo.ui.theme.repository.DestinoRepository
import com.example.appturismo.ui.theme.model.Destino

class DestinoViewModel : ViewModel() {

    fun obtenerDestinos(): List<Destino> {
        return DestinoRepository.obtenerDestinos()
    }

    fun obtenerDestino(id: Int): Destino? {
        return DestinoRepository.obtenerDestinoPorId(id)
    }

}