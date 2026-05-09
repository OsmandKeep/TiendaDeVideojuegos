package com.example.tiendadevideojuegos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tiendadevideojuegos.HomeScreen
import com.example.tiendadevideojuegos.LibraryScreen
import com.example.tiendadevideojuegos.CartScreen

@Composable
fun SimpleNavBar(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,

                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,

                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
                )

                NavigationBarItem(
                    selected = currentScreen == 0,
                    onClick = { onScreenChange(0) },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.iconomenugato),
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Inicio"
                        )
                    },
                    label = { Text("Inicio") },
                    colors = itemColors
                )

                NavigationBarItem(
                    selected = currentScreen == 1,
                    onClick = { onScreenChange(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    },
                    label = { Text("Carrito") },
                    colors = itemColors
                )

                NavigationBarItem(
                    selected = currentScreen == 2,
                    onClick = { onScreenChange(2) },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.iconobibliotecagato),
                            modifier = Modifier.size(30.dp),
                            contentDescription = "Biblioteca"
                        )
                    },
                    label = { Text("Biblioteca") },
                    colors = itemColors
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    0 -> HomeScreen()
                    1 -> CartScreen()
                    2 -> LibraryScreen()
                }
            }
        }
    }
}