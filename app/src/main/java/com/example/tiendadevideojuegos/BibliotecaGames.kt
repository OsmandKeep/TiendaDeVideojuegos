package com.example.tiendadevideojuegos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GameItem(
    val name: String,
    val status: String,
    val achievements: String,
    val icon: ImageVector
)

@Composable
fun LibraryScreen() {
    var selectedGame by remember { mutableStateOf("") }

    val games = listOf(
        GameItem("Mortal Kombat", "NUEVO", "0/19", Icons.Default.Person),
        GameItem("BroTato", "Tiempo: 1H", "5/10", Icons.Default.Face),
        GameItem("Elden Ring", "Tiempo: 120H", "20/23", Icons.Default.Casino),
        GameItem("Slime Rancher", "Tiempo: 20H", "1/10", Icons.Default.Pets),
        GameItem("Sonic", "Tiempo: 10H", "10/20", Icons.Default.DirectionsRun),
        GameItem("FNAF", "Tiempo: 40D", "20/23", Icons.Default.Warning)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
            Surface(
                modifier = Modifier.weight(1f).height(40.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text(" Buscar...", modifier = Modifier.padding(start = 8.dp), fontSize = 14.sp)
                }
            }
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(50.dp))
        }

        Text(
            text = "BIBLIOTECA",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(games.size) { index ->
                val game = games[index]
                GameCard(
                    game = game,
                    isSelected = selectedGame == game.name,
                    onSelect = { selectedGame = game.name }
                )
            }
        }
    }
}

@Composable
fun GameCard(
    game: GameItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(Color.Gray.copy(alpha = 0.1f))) {

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = game.achievements,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }

            Icon(
                game.icon,
                contentDescription = null,
                modifier = Modifier.size(50.dp).align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = game.name,
                modifier = Modifier.align(Alignment.BottomStart).padding(6.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        val isDarkTheme = isSystemInDarkTheme()

        val statusBackgroundColor = if (isDarkTheme) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.primaryFixedDim
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(statusBackgroundColor)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = game.status,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}