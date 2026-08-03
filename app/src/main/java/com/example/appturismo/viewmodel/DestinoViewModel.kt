package com.example.appturismo.viewmodel


import androidx.lifecycle.ViewModel
import com.example.appturismo.data.database.repository.DestinoRepository
import com.example.appturismo.data.database.model.Destino

class DestinoViewModel : ViewModel() {

    fun obtenerDestinos(): List<Destino> {
        return DestinoRepository.obtenerDestinos()
    }

    fun obtenerDestino(id: Int): Destino? {
        return DestinoRepository.obtenerDestinoPorId(id)
    }

}