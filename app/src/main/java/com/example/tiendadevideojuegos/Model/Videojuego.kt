package com.example.tiendadevideojuegos.Model

// Estructura de datos para mapear con Firestore
data class Videojuego(
    val id: String = "",
    val titulo: String = "",
    val fecha: String = "",
    val desarrollador: String = "",
    val descripcion: String = "",
    val requisitos: String = "",
    val etiquetas: List<String> = emptyList(),
    val likes: String = "0",
    val dislikes: String = "0"
)