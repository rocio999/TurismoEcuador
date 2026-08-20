package com.example.appturismo.ui.theme.components



import android.view.MotionEvent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaUbicacion(
    latitud: Double,
    longitud: Double
) {

    val context = LocalContext.current

    /*
     * Configuración de osmdroid
     */
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences(
            "osmdroid",
            0
        )
    )

    /*
     * Crear el mapa
     */
    AndroidView(

        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),

        factory = {

            MapView(it).apply {

                /*
                 * Fuente del mapa
                 */
                setTileSource(
                    TileSourceFactory.MAPNIK
                )

                /*
                 * Permitir zoom
                 */
                setMultiTouchControls(true)

                /*
                 * Zoom inicial
                 */
                controller.setZoom(16.0)

                /*
                 * Posición del usuario
                 */
                val posicion = GeoPoint(
                    latitud,
                    longitud
                )

                /*
                 * Centrar mapa
                 */
                controller.setCenter(
                    posicion
                )

                /*
                 * Marcador
                 */
                val marcador = Marker(this)

                marcador.position = posicion

                marcador.title = "Mi ubicación"

                marcador.snippet =
                    "Ubicación actual"

                overlays.add(marcador)

                /*
                 * Actualizar el mapa
                 */
                invalidate()
            }
        },

        update = { mapa ->

            /*
             * Nueva posición
             */
            val posicion = GeoPoint(
                latitud,
                longitud
            )

            /*
             * Centrar nuevamente
             */
            mapa.controller.animateTo(
                posicion
            )

            /*
             * Actualizar marcador
             */
            val marcadorExistente =
                mapa.overlays
                    .filterIsInstance<Marker>()
                    .firstOrNull()

            if (marcadorExistente != null) {

                marcadorExistente.position =
                    posicion

                marcadorExistente.title =
                    "Mi ubicación"

            } else {

                val nuevoMarcador =
                    Marker(mapa)

                nuevoMarcador.position =
                    posicion

                nuevoMarcador.title =
                    "Mi ubicación"

                mapa.overlays.add(
                    nuevoMarcador
                )
            }

            mapa.invalidate()
        }
    )
}