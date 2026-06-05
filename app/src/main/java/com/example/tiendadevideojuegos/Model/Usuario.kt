package com.example.tiendadevideojuegos.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val deseados: List<String> = emptyList(),
    val carrito: List<String> = emptyList(),
    val biblioteca: List<String> = emptyList()
)