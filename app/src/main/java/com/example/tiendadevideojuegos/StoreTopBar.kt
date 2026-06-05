package com.example.tiendadevideojuegos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StoreTopBar(
    onUserClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.BookmarkBorder,
            contentDescription = null,
            tint = colores.primary,
            modifier = Modifier.size(40.dp)
        )

        Surface(
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            color = colores.surfaceVariant,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = " Buscar...",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp,
                    color = colores.onSurfaceVariant
                )
            }
        }

        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Perfil de Usuario",
            tint = colores.primary,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .clickable { onUserClick() }
        )
    }
}