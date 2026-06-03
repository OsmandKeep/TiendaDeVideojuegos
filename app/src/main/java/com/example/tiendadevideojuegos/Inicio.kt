package com.example.tiendadevideojuegos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onGameClick: (String) -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(horizontal = 16.dp)
    ) {
        // ELIMINADO: StoreTopSection(colores) ya no se invoca aquí de forma local

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Novedades",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            NovedadesCarousel(colores, onGameClick)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recomendado Para ti",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            RecomendadosCarousel(colores, onGameClick)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun NovedadesCarousel(colores: ColorScheme, onGameClick: (String) -> Unit) {
    val listaNovedades = listOf(
        Juego(1, "Mortal Kombat 11", R.drawable.mk11, "mortal_kombat_11"),
        Juego(2, "Resident Evil 4", R.drawable.re4, "resident_evil_4"),
        Juego(3, "Sonic Mania", R.drawable.sonicman, "sonic_mania")
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listaNovedades) { juego ->
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(280.dp)
                    .height(200.dp)
                    .clickable { onGameClick(juego.firestoreId) },
                colors = CardDefaults.cardColors(containerColor = colores.surfaceVariant)
            ) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        Image(
                            painter = painterResource(id = juego.imagenResId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(colores.primaryContainer.copy(alpha = 0.8f), CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = colores.onPrimaryContainer)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(juego.titulo, fontWeight = FontWeight.Bold, color = colores.onSurfaceVariant)
                        Icon(Icons.Outlined.FavoriteBorder, null, modifier = Modifier.size(20.dp), tint = colores.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun RecomendadosCarousel(colores: ColorScheme, onGameClick: (String) -> Unit) {
    val lista = listOf(
        Recomendado(1, "Clash Royale", "Estrategia", R.drawable.clashroyale, "clash_royale"),
        Recomendado(2, "Fortnite", "Shooter", R.drawable.fortnite, "fortnite"),
        Recomendado(3, "Halo: MCC", "Shooter", R.drawable.halo, "halo_mcc"),
        Recomendado(4, "Elden Ring", "RPG", R.drawable.eldenring, "elden_ring"),
        Recomendado(5, "Minecraft", "Aventura", R.drawable.minecraft, "minecraft"),
        Recomendado(6, "FNAF", "Terror", R.drawable.fnaf, "fnaf")
    )

    val columnas = lista.chunked(3)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(columnas) { columna ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                columna.forEach { app ->
                    AppItem(app, colores, onGameClick)
                }
            }
        }
    }
}

@Composable
fun AppItem(app: Recomendado, colores: ColorScheme, onGameClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .width(300.dp)
            .clickable { onGameClick(app.firestoreId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = app.iconoResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(14.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.titulo, fontWeight = FontWeight.Medium, color = colores.onBackground, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(app.categoria, color = colores.onSurfaceVariant, fontSize = 13.sp)
        }
        Button(
            onClick = { onGameClick(app.firestoreId) },
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colores.secondaryContainer, contentColor = colores.onSecondaryContainer)
        ) {
            Text("Ver", fontSize = 12.sp)
        }
    }
}

// Modelos locales
data class Juego(val id: Int, val titulo: String, val imagenResId: Int, val firestoreId: String)
data class Recomendado(val id: Int, val titulo: String, val categoria: String, val iconoResId: Int, val firestoreId: String)