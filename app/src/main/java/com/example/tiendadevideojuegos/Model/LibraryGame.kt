package com.example.tiendadevideojuegos.Model

data class LibraryGame(
    val id: String = "",
    val titulo: String = "",
    val imagenUrl: String = "",
    val logros: String = "0/0" // Campo dinámico desde Firestore
)