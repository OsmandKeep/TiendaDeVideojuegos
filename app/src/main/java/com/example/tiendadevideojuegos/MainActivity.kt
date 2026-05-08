package com.example.tiendadevideojuegos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiendadevideojuegos.ui.theme.TiendaDeVideojuegosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaDeVideojuegosTheme {
                var selectedTab by remember { mutableStateOf(0) }

                SimpleNavBar(
                    currentScreen = selectedTab,
                    onScreenChange = { selectedTab = it }
                )
            }
        }
    }
}
@Composable
fun App (){
    Text("solo sirve para concetar las pantallas")
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pantalla de Inicio")
        Text("Bienvenido a la tienda de videojuegos")
    }
}