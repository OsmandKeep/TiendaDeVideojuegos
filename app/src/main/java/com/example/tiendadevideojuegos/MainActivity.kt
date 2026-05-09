package com.example.tiendadevideojuegos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.tiendadevideojuegos.ui.theme.TiendaDeVideojuegosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaDeVideojuegosTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        AppNavigation(onLoginSuccess = { isLoggedIn = true })
    } else {
        MainAppContent()
    }
}

@Composable
fun AppNavigation(onLoginSuccess: () -> Unit) {
    var currentScreen by remember { mutableStateOf("login") }

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginClick = { onLoginSuccess() },
            onRegisterClick = { currentScreen = "register" },
            onForgotClick = { currentScreen = "forgot" }
        )
        "register" -> RegisterScreen(onBackToLogin = { currentScreen = "login" })
        "forgot" -> ForgotPasswordScreen(onBackToLogin = { currentScreen = "login" })
    }
}

@Composable
fun MainAppContent() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            SimpleNavBar(
                currentScreen = selectedTab,
                onScreenChange = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {

                0 -> MenuPrincipal()
                1 -> CartScreen()
                2 -> Text("Favoritos")
                3 -> Text("Perfil")
            }
        }
    }
}