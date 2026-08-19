package com.example.appturismo.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.data.database.datastore.PreferenciasDataStore
import com.example.appturismo.viewmodel.PreferenciasViewModel
import com.example.appturismo.viewmodel.PreferenciasViewModelFactory

@Composable
fun AjustesScreen() {

    val context = LocalContext.current

    val preferenciasDataStore = remember {
        PreferenciasDataStore(context)
    }

    val preferenciasViewModel: PreferenciasViewModel = viewModel(
        factory = PreferenciasViewModelFactory(
            preferenciasDataStore
        )
    )

    val modoOscuro by preferenciasViewModel.modoOscuro
        .collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "⚙️ Ajustes",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "🌙 Modo oscuro",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = if (modoOscuro) {
                        "Activado"
                    } else {
                        "Desactivado"
                    }
                )
            }

            Switch(
                checked = modoOscuro,
                onCheckedChange = { activado ->

                    preferenciasViewModel
                        .cambiarModoOscuro(activado)
                }
            )
        }
    }
}