package com.example.tiendadevideojuegos

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendadevideojuegos.ui.theme.TiendaDeVideojuegosTheme

@Composable
fun GameDetailScreen() {
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
    ) {
        StoreTopSection(colores)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colores.surfaceVariant)
        ) {
            Icon(
                Icons.Default.Gamepad,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(40.dp),
                tint = colores.onSurfaceVariant.copy(alpha = 0.3f)
            )

            Text(
                text = "Mortal 11 Kombat",
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
            // Información principal y botón deseos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mortal 11 Kombat", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Fecha: 9/03/2019", fontSize = 14.sp, color = colores.onSurfaceVariant)
                    Text("Desarrollador: Warner Bros", fontSize = 14.sp, color = colores.onSurfaceVariant)
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier.border(1.dp, colores.primary, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = colores.primary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Etiquetas
            Text("Etiquetas:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Lucha", "Multijugador", "2.5D").forEach { etiqueta ->
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
                Icon(Icons.Default.AddBox, null, tint = colores.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Comprar
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Estado: ", fontWeight = FontWeight.Bold)
                Button(onClick = { }, shape = RoundedCornerShape(4.dp)) {
                    Text("Comprar")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Preview y Descripción
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.weight(1.2f).height(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                    Icon(Icons.Default.ChevronLeft, null, tint = Color.White, modifier = Modifier.align(Alignment.CenterStart))
                    Icon(Icons.Default.ChevronRight, null, tint = Color.White, modifier = Modifier.align(Alignment.CenterEnd))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Descripción:", fontWeight = FontWeight.Bold)
                    Text("MK regresando mejor que nunca con peleas intensas.", fontSize = 13.sp)
                }
            }

            // Reseñas
            Spacer(modifier = Modifier.height(20.dp))
            Text("Reseñas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Row(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ReseñaCard("150 K", Icons.Default.ThumbUp, colores.primary, Modifier.weight(1f))
                ReseñaCard("10 K", Icons.Default.ThumbDown, colores.error, Modifier.weight(1f))
            }

            // Requisitos
            Text("Requisitos Mínimos", fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 2.dp, color = colores.primary)
            Text("Procesador i5, 8GB RAM, GTX 1050...", fontSize = 13.sp)

            Spacer(modifier = Modifier.height(80.dp))
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

@Composable
fun StoreBottomNavBar() {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.iconomenugato),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Inicio"
                )
            },
            label = { Text("Inicio") },
            selected = true,
            onClick = {}
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Tienda"
                )
            },
            label = { Text("Tienda") },
            selected = false,
            onClick = {}
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.iconobibliotecagato),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Biblioteca"
                )
            },
            label = { Text("Biblioteca") },
            selected = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_7", showSystemUi = true)
@Composable
fun GameDetailPreview() {
    TiendaDeVideojuegosTheme {
        Scaffold(
            bottomBar = { StoreBottomNavBar() }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                GameDetailScreen()
            }
        }
    }
}