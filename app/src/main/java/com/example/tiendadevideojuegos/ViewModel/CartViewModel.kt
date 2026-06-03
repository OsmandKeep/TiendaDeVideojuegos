package com.example.tiendadevideojuegos

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val cartItems = mutableStateListOf<CartItem>()

    // GUARDAR EL LISTENER: Esta variable mantendrá la conexión activa de Firestore
    private var carritoListener: ListenerRegistration? = null

    init {
        // Escuchamos los cambios de autenticación (Login / Logout / Cambio de cuenta)
        auth.addAuthStateListener { firebaseAuth ->
            val usuarioActual = firebaseAuth.currentUser
            if (usuarioActual != null) {
                // Si entra un usuario, destruimos cualquier listener viejo y creamos uno nuevo limpio
                reiniciarListener()
            } else {
                // Si cierra sesión, limpiamos la interfaz y destruimos el listener por completo
                limpiarTodo()
            }
        }
    }

    private fun reiniciarListener() {
        // Apagamos el listener anterior antes de encender el nuevo
        carritoListener?.remove()
        cartItems.clear()

        val uid = auth.currentUser?.uid ?: return

        // Guardamos la nueva conexión en la variable para poder controlarla
        carritoListener = db.collection("usuarios").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val idsEnCarrito = snapshot.get("carrito") as? List<String> ?: emptyList()

                if (idsEnCarrito.isEmpty()) {
                    cartItems.clear()
                    return@addSnapshotListener
                }

                viewModelScope.launch {
                    try {
                        val listaTemporal = mutableListOf<CartItem>()
                        for (idJuego in idsEnCarrito) {
                            val juegoDoc = db.collection("videojuegos").document(idJuego).get().await()
                            val juego = juegoDoc.toObject(CartItem::class.java)?.copy(id = juegoDoc.id)
                            if (juego != null) {
                                listaTemporal.add(juego)
                            }
                        }
                        cartItems.clear()
                        cartItems.addAll(listaTemporal)
                    } catch (e: Exception) {
                        // Manejo de errores de red
                    }
                }
            }
    }

    private fun limpiarTodo() {
        carritoListener?.remove() // Desconecta la escucha a Firestore de la cuenta vieja
        carritoListener = null
        cartItems.clear()         // Vacía la pantalla de inmediato
    }

    fun eliminarDelCarrito(juegoId: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .update("carrito", FieldValue.arrayRemove(juegoId))
    }

    fun limpiarCarrito() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("usuarios").document(uid)
            .update("carrito", emptyList<String>())
    }

    // NUEVO: Procesa la compra moviendo los IDs de "carrito" a "biblioteca" en una transacción atómica
    fun procesarCompraExitosa(onResultado: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        // Obtenemos los IDs actuales del carrito que están cargados en memoria
        val idsComprados = cartItems.map { it.id }
        if (idsComprados.isEmpty()) {
            onResultado(false)
            return
        }

        val usuarioRef = db.collection("usuarios").document(uid)

        db.runTransaction { transaction ->
            // 1. Agrega los IDs de los videojuegos al arreglo "biblioteca" del usuario
            transaction.update(usuarioRef, "biblioteca", FieldValue.arrayUnion(*idsComprados.toTypedArray()))

            // 2. Vacía por completo el arreglo de "carrito" en el mismo proceso
            transaction.update(usuarioRef, "carrito", emptyList<String>())
        }.addOnSuccessListener {
            cartItems.clear() // Limpia la UI local inmediatamente tras la confirmación de Firestore
            onResultado(true)
        }.addOnFailureListener {
            onResultado(false)
        }
    }

    // Si destruyen la pantalla por completo, aseguramos liberar la memoria de Firebase
    override fun onCleared() {
        super.onCleared()
        carritoListener?.remove()
    }
}