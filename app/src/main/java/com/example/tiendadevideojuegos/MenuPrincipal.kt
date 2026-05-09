package com.example.tiendadevideojuegos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MenuPrincipal() {
    // Estado para saber qué pestaña está seleccionada
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            // Llamamos a tu barra de navegación
            SimpleNavBar(
                currentScreen = selectedTab,
                onScreenChange = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            // Aquí se intercambian las pantallas según el índice
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> CartScreen()
                2 -> LibraryScreen()
            }
        }
    }
}