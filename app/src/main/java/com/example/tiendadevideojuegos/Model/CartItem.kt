package com.example.tiendadevideojuegos

// Esta clase sirve tanto para el Carrito como para mapear los datos base de Firestore
data class CartItem(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val desarrollador: String = "",
    val fecha: String = "",
    val imagenUrl: String = "",
    val precioOriginal: Double = 0.0, // <-- Mapea directamente el "number" de Firebase
    val descuento: Int = 0            // <-- Mapea directamente el "number" de Firebase
) {
    // Propiedad calculada automáticamente en base a los campos de la BD
    val finalPrice: Double
        get() = precioOriginal * (1 - descuento / 100.0)
}