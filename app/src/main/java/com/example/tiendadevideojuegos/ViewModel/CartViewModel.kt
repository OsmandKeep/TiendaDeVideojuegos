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

    private var carritoListener: ListenerRegistration? = null

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val usuarioActual = firebaseAuth.currentUser
            if (usuarioActual != null) {
                reiniciarListener()
            } else {
                limpiarTodo()
            }
        }
    }

    private fun reiniciarListener() {
        carritoListener?.remove()
        cartItems.clear()

        val uid = auth.currentUser?.uid ?: return

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
                    }
                }
            }
    }

    private fun limpiarTodo() {
        carritoListener?.remove()
        carritoListener = null
        cartItems.clear()
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

    fun procesarCompraExitosa(onResultado: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        val idsComprados = cartItems.map { it.id }
        if (idsComprados.isEmpty()) {
            onResultado(false)
            return
        }

        val usuarioRef = db.collection("usuarios").document(uid)

        db.runTransaction { transaction ->
            transaction.update(usuarioRef, "biblioteca", FieldValue.arrayUnion(*idsComprados.toTypedArray()))

            transaction.update(usuarioRef, "carrito", emptyList<String>())
        }.addOnSuccessListener {
            cartItems.clear()
            onResultado(true)
        }.addOnFailureListener {
            onResultado(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        carritoListener?.remove()
    }
}
