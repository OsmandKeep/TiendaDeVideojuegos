package com.example.tiendadevideojuegos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tiendadevideojuegos.Model.LibraryGame
import com.example.tiendadevideojuegos.ViewModel.LibraryViewModel

@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel = viewModel()
) {
    var selectedGameId by remember { mutableStateOf("") }
    val colores = MaterialTheme.colorScheme
    val games = libraryViewModel.libraryItems

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "BIBLIOTECA",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (games.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes videojuegos comprados.",
                    color = colores.onSurfaceVariant,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(games) { game ->
                    GameCard(
                        game = game,
                        isSelected = selectedGameId == game.id,
                        onSelect = { selectedGameId = game.id }
                    )
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: LibraryGame,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val borderColor = if (isSelected) colores.primary else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .background(colores.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            if (game.imagenUrl.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().background(colores.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Gamepad, null, tint = colores.primary, modifier = Modifier.size(40.dp))
                }
            } else {
                AsyncImage(
                    model = game.imagenUrl,
                    contentDescription = game.titulo,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // ETIQUETA DE LOGROS
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                color = colores.secondaryContainer.copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = game.logros,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Text(
                text = game.titulo,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        val isDarkTheme = isSystemInDarkTheme()
        val statusBackgroundColor = if (isDarkTheme) colores.primaryContainer else colores.surfaceContainerHighest

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(statusBackgroundColor)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LISTO PARA JUGAR",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colores.onPrimaryContainer
            )
        }
    }
}