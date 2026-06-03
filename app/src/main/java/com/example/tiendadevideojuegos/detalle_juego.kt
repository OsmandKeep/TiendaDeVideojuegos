package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue // <-- IMPORTANTE: Soluciona el error de Property delegate
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendadevideojuegos.ViewModel.GameDetailViewModel

@Composable
fun GameDetailScreen(
    gameId: String,
    viewModel: GameDetailViewModel = viewModel()
) {
    val colores = MaterialTheme.colorScheme
    val context = LocalContext.current
    val juego by viewModel.videojuegoState  // Ahora funciona correctamente gracias al import de getValue
    val isLoading by viewModel.isLoading

    LaunchedEffect(gameId) {
        viewModel.obtenerDetallesDelJuego(gameId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colores.primary)
        }
    } else if (juego != null) {
        // Desestructuración segura eliminando errores de Smart Cast
        val currentGame = juego!!
        val precioOriginal = currentGame.precioOriginal
        val porcentajeDescuento = currentGame.descuento
        val precioFinal = precioOriginal * (1 - porcentajeDescuento / 100.0)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colores.background)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colores.surfaceVariant)
            ) {
                if (currentGame.imagenUrl.isEmpty()) {
                    Icon(
                        Icons.Default.Gamepad,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(40.dp),
                        tint = colores.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                } else {
                    AsyncImage(
                        model = currentGame.imagenUrl,
                        contentDescription = "Banner de ${currentGame.titulo}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = currentGame.titulo,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = currentGame.titulo, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Text(text = "Fecha: ${currentGame.fecha}", fontSize = 14.sp, color = colores.onSurfaceVariant)
                        Text(text = "Desarrollador: ${currentGame.desarrollador}", fontSize = 14.sp, color = colores.onSurfaceVariant)
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier.border(1.dp, colores.primary, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = colores.primary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Etiquetas:", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                // Corrección del flujo Composable para las etiquetas
                Row(
                    modifier = Modifier.padding(vertical = 4.dp).horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (etiqueta in currentGame.etiquetas) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = colores.secondaryContainer,
                            border = BorderStroke(1.dp, colores.outline)
                        ) {
                            Text(
                                text = etiqueta,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = colores.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            if (porcentajeDescuento > 0) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = colores.tertiary,
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Text(
                                            text = "-$porcentajeDescuento%",
                                            color = colores.onTertiary,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "Mex$ ${String.format("%.2f", precioOriginal)}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        ),
                                        color = colores.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                            Text(
                                text = "Mex$ ${String.format("%.2f", precioFinal)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colores.onSurface
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.añadirAlCarrito(gameId) { exitoso ->
                                    if (exitoso) {
                                        Toast.makeText(context, "¡Añadido al carrito con éxito!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error al añadir o sesión expirada.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
                        ) {
                            Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Añadir al carrito", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Descripción:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = currentGame.descripcion, fontSize = 13.sp)

                    if (currentGame.capturas.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Capturas de pantalla:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().height(150.dp)
                        ) {
                            items(currentGame.capturas) { urlCaptura ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.width(260.dp).fillMaxHeight(),
                                    colors = CardDefaults.cardColors(containerColor = colores.surfaceVariant)
                                ) {
                                    AsyncImage(
                                        model = urlCaptura,
                                        contentDescription = "Captura de pantalla",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Reseñas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ReseñaCard(currentGame.likes, Icons.Default.ThumbUp, colores.primary, Modifier.weight(1f))
                    ReseñaCard(currentGame.dislikes, Icons.Default.ThumbDown, colores.error, Modifier.weight(1f))
                }

                Text("Requisitos Mínimos", fontWeight = FontWeight.Bold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 2.dp, color = colores.primary)
                Text(text = currentGame.requisitos, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se pudo cargar la información de este videojuego.", color = colores.error)
        }
    }
}

@Composable
fun ReseñaCard(count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = modifier) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(count, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(icon, null, modifier = Modifier.size(18.dp), tint = color)
        }
    }
}