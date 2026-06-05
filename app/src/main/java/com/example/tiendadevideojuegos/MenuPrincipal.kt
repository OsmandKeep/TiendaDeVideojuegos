package com.example.tiendadevideojuegos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

@Composable
fun MenuPrincipal() {
    var selectedGameId by rememberSaveable { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (selectedGameId != null) {
            GameDetailScreen(gameId = selectedGameId!!)

        } else {
            HomeScreen(onGameClick = { idRecibido ->
                selectedGameId = idRecibido
            })
        }
    }
}
