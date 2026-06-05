import com.google.firebase.firestore.FirebaseFirestore
import com.example.tiendadevideojuegos.model.Usuario

fun registrarUsuarioEnBaseDeDatos(idUsuario: String, nombreUsuario: String) {
    val db = FirebaseFirestore.getInstance()

    val nuevoUsuario = Usuario(
        id = idUsuario,
        nombre = nombreUsuario,
        deseados = emptyList(),
        carrito = emptyList(),
        biblioteca = emptyList()
    )

    // Guardar el documento
    db.collection("usuarios")
        .document(idUsuario)
        .set(nuevoUsuario)
        .addOnSuccessListener {
            println("Usuario registrado exitosamente con sus contenedores listos.")
        }
        .addOnFailureListener { error ->
            println("Error al crear la cuenta del usuario: ${error.message}")
        }
}