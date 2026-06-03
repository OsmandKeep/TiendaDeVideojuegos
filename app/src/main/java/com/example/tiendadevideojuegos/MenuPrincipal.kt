package com.example.tiendadevideojuegos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable // <--- AGREGADO PARA QUE NO SE INTERRUMPA EL ESTADO
import androidx.compose.ui.Modifier

@Composable
fun MenuPrincipal() {
    // Este estado recordará qué juego seleccionó el usuario. Si es null, muestra la tienda.
    var selectedGameId by rememberSaveable { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (selectedGameId != null) {
            // Si hay un juego seleccionado, abrimos la pantalla con datos de Firebase
            GameDetailScreen(gameId = selectedGameId!!)

            // TIP PRO: Si quisieras un botón para regresar a la tienda, podrías pasarle un callback
            // aquí a tu pantalla de detalle, por ejemplo: onBackClick = { selectedGameId = null }
        } else {
            // Si no hay juego seleccionado, cargamos tu catálogo normal
            HomeScreen(onGameClick = { idRecibido ->
                // En cuanto el usuario pulse un juego del carrusel, guardamos su ID de Firebase
                selectedGameId = idRecibido
            })
        }
    }
}