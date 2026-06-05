package com.example.tiendadevideojuegos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SimpleNavBar(
    currentScreen: Int,
    onScreenChange: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (drawerState.isOpen) {
        androidx.activity.compose.BackHandler {
            scope.launch { drawerState.close() }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        modifier = Modifier.width(280.dp)
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Mi Perfil",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        NavigationDrawerItem(
                            label = { Text("Cerrar Sesión") },
                            selected = false,
                            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") },
                            onClick = {
                                scope.launch { drawerState.close() }
                                onLogout()
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    topBar = {
                        StoreTopBar(onUserClick = {
                            scope.launch { drawerState.open() }
                        })
                    },
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

                            // 0 -> Inicio
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

                            // 1 -> Carrito
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

                            // 2 -> Biblioteca
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
                                0 -> MenuPrincipal()
                                1 -> CartScreen()
                                2 -> LibraryScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
