package com.example.tiendadevideojuegos.ViewModel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.tiendadevideojuegos.Model.Videojuego
import com.google.firebase.firestore.FirebaseFirestore

class GameDetailViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Estado que observará la pantalla de Compose
    private val _videojuegoState = mutableStateOf<Videojuego?>(null)
    val videojuegoState: State<Videojuego?> = _videojuegoState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Función para traer la información de un juego usando su ID único de Firestore
    fun obtenerDetallesDelJuego(idJuego: String) {
        _isLoading.value = true
        db.collection("videojuegos").document(idJuego)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Mapea el documento directamente a nuestro objeto Kotlin
                    val juego = document.toObject(Videojuego::class.java)?.copy(id = document.id)
                    _videojuegoState.value = juego
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
                // Aquí puedes manejar errores si la lectura falla
            }
    }
}