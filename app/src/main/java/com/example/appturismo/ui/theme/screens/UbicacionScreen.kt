package com.example.appturismo.ui.theme.screens


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
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

    val ubicacion by viewModel.ubicacion.collectAsState()

    var permisoDenegado by remember {
        mutableStateOf(false)
    }

    val permisoLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->

            val tienePermiso =
                permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (tienePermiso) {

                permisoDenegado = false

                viewModel.obtenerUbicacion(context)

            } else {

                permisoDenegado = true
            }
        }

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

        if (permisoPreciso || permisoAproximado) {

            viewModel.obtenerUbicacion(context)

        } else {

            permisoLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "📍 Mi ubicación",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (ubicacion != null) {

            Text(
                text = "Latitud: ${ubicacion!!.latitude}"
            )

            Text(
                text = "Longitud: ${ubicacion!!.longitude}"
            )

        } else {

            Text(
                text = "Aún no hemos obtenido tu ubicación."
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                solicitarUbicacion()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("📍 Obtener mi ubicación")
        }

        if (permisoDenegado) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Necesitamos permiso de ubicación para obtener tu posición.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}