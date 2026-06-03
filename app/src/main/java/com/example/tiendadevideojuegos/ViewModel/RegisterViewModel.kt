import com.google.firebase.firestore.FirebaseFirestore
import com.example.tiendadevideojuegos.model.Usuario

fun registrarUsuarioEnBaseDeDatos(idUsuario: String, nombreUsuario: String) {
    // 1. Obtener la instancia de Firestore
    val db = FirebaseFirestore.getInstance()

    // 2. Crear el objeto Usuario con los datos iniciales (las listas van vacías)
    val nuevoUsuario = Usuario(
        id = idUsuario,
        nombre = nombreUsuario,
        deseados = emptyList(),
        carrito = emptyList(),
        biblioteca = emptyList()
    )

    // 3. Guardar el documento en la colección "usuarios" usando su ID único
    db.collection("usuarios")
        .document(idUsuario)
        .set(nuevoUsuario) // .set reemplaza o crea el documento con este objeto estructurado
        .addOnSuccessListener {
            // ¡Cuenta inicializada con éxito en la nube!
            println("Usuario registrado exitosamente con sus contenedores listos.")
        }
        .addOnFailureListener { error ->
            // Manejo de errores por si falla el internet
            println("Error al crear la cuenta del usuario: ${error.message}")
        }
}