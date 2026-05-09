package com.example.tiendadevideojuegos

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CartItem(
    val name: String,
    val originalPrice: Double,
    val discountPercentage: Int,
    val icon: ImageVector
) {
    val finalPrice: Double
        get() = originalPrice * (1 - discountPercentage / 100.0)
}

@Composable
fun CartScreen() {
    val colores = MaterialTheme.colorScheme

    val cartItems = listOf(
        CartItem("Trepang2", 349.99, 66, Icons.Default.Shield),
        CartItem("CODE VEIN Deluxe Edition", 1499.00, 85, Icons.Default.Casino),
        CartItem("Mortal Kombat 11", 150.00, 0, Icons.Default.Person),
        CartItem("Elden Ring", 1200.00, 10, Icons.Default.Star)
    )

    val total = cartItems.sumOf { it.finalPrice }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(horizontal = 16.dp)
    ) {
        HeaderSection()

        Text(
            text = "Carrito de Compras",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(cartItems) { item ->
                CartItemCard(item)
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

            Button(
                onClick = { },
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
fun CartItemCard(item: CartItem) {
    val colores = MaterialTheme.colorScheme
    val accentColor = colores.primary

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
                .background(accentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, null, tint = accentColor, modifier = Modifier.size(35.dp))
        }

        Text(
            text = item.name,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colores.onSurfaceVariant,
            maxLines = 2
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.discountPercentage > 0) {
                Surface(
                    color = colores.tertiary,
                    modifier = Modifier.padding(end = 6.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "-${item.discountPercentage}%",
                        color = colores.onTertiary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (item.discountPercentage > 0) {
                    Text(
                        text = "Mex$ ${String.format("%.2f", item.originalPrice)}",
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

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = colores.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun HeaderSection() {
    val colores = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(Icons.Default.BookmarkBorder, null, tint = colores.primary, modifier = Modifier.size(40.dp))
        Surface(
            modifier = Modifier.weight(1f).height(40.dp),
            color = colores.surfaceVariant,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                Text(" Buscar...", modifier = Modifier.padding(start = 8.dp), fontSize = 14.sp)
            }
        }
        Icon(Icons.Default.AccountCircle, null, tint = colores.primary , modifier = Modifier.size(50.dp))
    }
}