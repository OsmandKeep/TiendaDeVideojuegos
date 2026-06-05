package com.example.tiendadevideojuegos

data class CartItem(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val desarrollador: String = "",
    val fecha: String = "",
    val imagenUrl: String = "",
    val precioOriginal: Double = 0.0,
    val descuento: Int = 0
) {
    val finalPrice: Double
        get() = precioOriginal * (1 - descuento / 100.0)
}