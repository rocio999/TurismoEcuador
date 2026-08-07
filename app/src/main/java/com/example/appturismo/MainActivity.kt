package com.example.appturismo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appturismo.data.database.datastore.PreferenciasDataStore
import com.example.appturismo.ui.theme.AppTurismoTheme
import com.example.appturismo.ui.theme.Navigation
import com.example.appturismo.viewmodel.PreferenciasViewModel
import com.example.appturismo.viewmodel.PreferenciasViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val context = this

            val preferenciasDataStore =
                PreferenciasDataStore(context)

            val preferenciasViewModel: PreferenciasViewModel =
                viewModel(
                    factory = PreferenciasViewModelFactory(
                        preferenciasDataStore
                    )
                )

            val modoOscuro by preferenciasViewModel.modoOscuro
                .collectAsStateWithLifecycle()

            AppTurismoTheme(
                darkTheme = modoOscuro
            ) {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        Navigation()
                    }
                }
            }
        }
    }
}