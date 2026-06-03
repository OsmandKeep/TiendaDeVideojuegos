package com.example.tiendadevideojuegos.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevideojuegos.Model.LibraryGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LibraryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val libraryItems = mutableStateListOf<LibraryGame>()
    private var libraryListener: ListenerRegistration? = null

    init {
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                reiniciarLibraryListener()
            } else {
                limpiarBiblioteca ()
            }
        }
    }

    private fun reiniciarLibraryListener() {
        libraryListener?.remove()
        libraryItems.clear()

        val uid = auth.currentUser?.uid ?: return

        libraryListener = db.collection("usuarios").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val idsEnBiblioteca = snapshot.get("biblioteca") as? List<String> ?: emptyList()

                if (idsEnBiblioteca.isEmpty()) {
                    libraryItems.clear()
                    return@addSnapshotListener
                }

                viewModelScope.launch {
                    try {
                        val listaTemporal = mutableListOf<LibraryGame>()
                        for (idJuego in idsEnBiblioteca) {
                            val juegoDoc = db.collection("videojuegos").document(idJuego).get().await()
                            val juego = juegoDoc.toObject(LibraryGame::class.java)?.copy(id = juegoDoc.id)
                            if (juego != null) {
                                listaTemporal.add(juego)
                            }
                        }
                        libraryItems.clear()
                        libraryItems.addAll(listaTemporal)
                    } catch (e: Exception) {
                        // Manejo de errores de conexión
                    }
                }
            }
    }

    private fun limpiarBiblioteca() {
        libraryListener?.remove()
        libraryListener = null
        libraryItems.clear()
    }

    override fun onCleared() {
        super.onCleared()
        libraryListener?.remove()
    }
}