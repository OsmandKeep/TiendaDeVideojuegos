package com.example.tiendadevideojuegos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiendadevideojuegos.ui.theme.TiendaDeVideojuegosTheme
// IMPORT DE FIREBASE
import com.google.firebase.auth.FirebaseAuth

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
    val auth = remember { FirebaseAuth.getInstance() }

    // TRUCO: Si el usuario ya existe localmente y está verificado, entra directo
    var isLoggedIn by remember {
        mutableStateOf(auth.currentUser != null && auth.currentUser!!.isEmailVerified)
    }

    if (!isLoggedIn) {
        AppNavigation(onLoginSuccess = { isLoggedIn = true })
    } else {
        // Le pasamos la función de cerrar sesión para que pueda regresar al Login
        MainAppContent(onLogout = {
            auth.signOut()
            isLoggedIn = false
        })
    }
}

@Composable
fun AppNavigation(onLoginSuccess: () -> Unit) {
    var currentScreen by rememberSaveable { mutableStateOf("login") }

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
fun MainAppContent(onLogout: () -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    // CORRECCIÓN: Le pasamos los tres parámetros requeridos por tu SimpleNavBar con Drawer
    SimpleNavBar(
        currentScreen = selectedTab,
        onScreenChange = { selectedTab = it },
        onLogout = onLogout // <-- Pasamos el callback de cierre de sesión hacia el menú lateral
    )
}