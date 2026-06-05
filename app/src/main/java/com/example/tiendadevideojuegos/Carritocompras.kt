package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = viewModel()
) {
    val colores = MaterialTheme.colorScheme
    val context = LocalContext.current
    val cartItems = cartViewModel.cartItems

    val total = cartItems.sumOf { it.finalPrice }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Carrito de Compras",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    color = colores.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(item, onDeleteClick = {
                        cartViewModel.eliminarDelCarrito(item.id)
                    })
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Total: Mex$ ${String.format("%.2f", total)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // BOTÓN DE ACCIÓN COMPRA TRANSACCIONAL ASOCIADO A BIBLIOTECA
            Button(
                onClick = {
                    if (cartItems.isNotEmpty()) {
                        cartViewModel.procesarCompraExitosa { exitoso ->
                            if (exitoso) {
                                Toast.makeText(
                                    context,
                                    "¡Compra realizada con éxito! Juegos añadidos a tu biblioteca.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Hubo un problema al procesar el pago. Inténtalo de nuevo.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                enabled = cartItems.isNotEmpty(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.primary,
                    contentColor = colores.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text("COMPRAR", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, onDeleteClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colores.surfaceVariant)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(colores.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            if (item.imagenUrl.isEmpty()) {
                Icon(Icons.Default.Gamepad, null, tint = colores.primary, modifier = Modifier.size(35.dp))
            } else {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.titulo,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            text = item.titulo,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colores.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.descuento > 0) {
                Surface(
                    color = colores.tertiary,
                    modifier = Modifier.padding(end = 6.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "-${item.descuento}%",
                        color = colores.onTertiary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (item.descuento > 0) {
                    Text(
                        text = "Mex$ ${String.format("%.2f", item.precioOriginal)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = colores.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = "Mex$ ${String.format("%.2f", item.finalPrice)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = colores.onSurface
                )
            }
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar del carrito",
                tint = colores.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}