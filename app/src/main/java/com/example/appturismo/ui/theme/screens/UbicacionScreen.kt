package com.example.appturismo.ui.theme.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.core.content.ContextCompat

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.appturismo.viewmodel.UbicacionViewModel


@Composable
fun UbicacionScreen(
    viewModel: UbicacionViewModel = viewModel()
) {

    val context = LocalContext.current

    val ubicacion by
    viewModel.ubicacion.collectAsState()

    var permisoDenegado by remember {
        mutableStateOf(false)
    }

    // SOLICITAR PERMISOS


    val permisoLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->

            val tienePermiso =
                permisos[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permisos[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (tienePermiso) {

                permisoDenegado = false

                viewModel.obtenerUbicacion(
                    context
                )

            } else {

                permisoDenegado = true
            }
        }



    // SOLICITAR UBICACIÓN


    fun solicitarUbicacion() {

        val permisoPreciso =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (
            permisoPreciso ||
            permisoAproximado
        ) {

            viewModel.obtenerUbicacion(
                context
            )

        } else {

            permisoLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }



    // ABRIR MAPA DEL TELÉFONO


    fun abrirMapa() {

        val location = ubicacion

        if (location == null) {

            Toast.makeText(
                context,
                "Primero debes obtener tu ubicación",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val latitud = location.latitude
        val longitud = location.longitude


        // INTENT PARA GOOGLE MAPS


        val googleMapsUri = Uri.parse(
            "geo:$latitud,$longitud?q=$latitud,$longitud"
        )

        val googleMapsIntent = Intent(
            Intent.ACTION_VIEW,
            googleMapsUri
        ).apply {

            setPackage(
                "com.google.android.apps.maps"
            )
        }

        try {

            context.startActivity(
                googleMapsIntent
            )

        } catch (
            error: ActivityNotFoundException
        ) {


            // SI GOOGLE MAPS NO ESTÁ INSTALADO
            // ABRIR GOOGLE MAPS EN EL NAVEGADOR


            val navegadorUri = Uri.parse(
                "https://www.google.com/maps/search/?api=1" +
                        "&query=$latitud,$longitud"
            )

            val navegadorIntent = Intent(
                Intent.ACTION_VIEW,
                navegadorUri
            )

            try {

                context.startActivity(
                    navegadorIntent
                )

            } catch (
                error2: ActivityNotFoundException
            ) {

                Toast.makeText(
                    context,
                    "No se encontró una aplicación para abrir el mapa",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }



    // INTERFAZ


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {


        // TÍTULO


        Text(

            text =
                "📍 Mi ubicación",

            style =
                MaterialTheme.typography.headlineMedium
        )


        Spacer(
            modifier =
                Modifier.height(16.dp)
        )



        // ESTADO


        if (ubicacion != null) {

            Text(

                text =
                    "Tu ubicación está disponible.",

                style =
                    MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )



            // ABRIR MAPA


            Button(

                onClick = {
                    abrirMapa()
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "🗺️ Ver mi ubicación en el mapa"
                )
            }

        } else {

            Text(

                text =
                    "Obtén tu ubicación para verla en el mapa.",

                style =
                    MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )



            // OBTENER UBICACIÓN


            Button(

                onClick = {
                    solicitarUbicacion()
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "📍 Obtener mi ubicación"
                )
            }
        }



        // PERMISO DENEGADO


        if (permisoDenegado) {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(

                text =
                    "Necesitamos permiso de ubicación para obtener tu posición.",

                style =
                    MaterialTheme.typography.bodyMedium
            )
        }
    }
}