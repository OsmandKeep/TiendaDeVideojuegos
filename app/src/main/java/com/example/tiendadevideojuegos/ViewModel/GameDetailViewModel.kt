package com.example.tiendadevideojuegos.ViewModel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// Estructura de datos explícita para evitar errores de referencia ("Unresolved reference Model")
data class Videojuego(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val desarrollador: String = "",
    val fecha: String = "",
    val imagenUrl: String = "",
    val requisitos: String = "",
    val likes: String = "0",
    val dislikes: String = "0",
    val etiquetas: List<String> = emptyList(),
    val capturas: List<String> = emptyList(),
    val precioOriginal: Double = 0.0,
    val descuento: Int = 0
)

class GameDetailViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Estado explícitamente tipado para evitar el error "Null cannot be a value of a non-null type"
    private val _videojuegoState = mutableStateOf<Videojuego?>(null)
    val videojuegoState: State<Videojuego?> = _videojuegoState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun obtenerDetallesDelJuego(idJuego: String) {
        _isLoading.value = true
        db.collection("videojuegos").document(idJuego)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Mapeo seguro corrigiendo el error de inferencia de tipos
                    val juego = document.toObject(Videojuego::class.java)?.copy(id = document.id)
                    _videojuegoState.value = juego
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }

    fun añadirAlCarrito(gameId: String, onResultado: (Boolean) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            db.collection("usuarios").document(uid)
                .update("carrito", FieldValue.arrayUnion(gameId))
                .addOnSuccessListener { onResultado(true) }
                .addOnFailureListener { onResultado(false) }
        } else {
            onResultado(false)
        }
    }
}