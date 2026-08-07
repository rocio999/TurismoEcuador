package com.example.appturismo.viewmodel


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UbicacionViewModel : ViewModel() {

    private val _ubicacion = MutableStateFlow<Location?>(null)
    val ubicacion: StateFlow<Location?> = _ubicacion

    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(context: Context) {

        viewModelScope.launch {

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {

                        _ubicacion.value = location

                        println(
                            "GPS: ${location.latitude}, ${location.longitude}"
                        )

                    } else {

                        println("GPS: ubicación no disponible")
                    }
                }
                .addOnFailureListener { error ->

                    println(
                        "GPS ERROR: ${error.message}"
                    )
                }
        }
    }
}