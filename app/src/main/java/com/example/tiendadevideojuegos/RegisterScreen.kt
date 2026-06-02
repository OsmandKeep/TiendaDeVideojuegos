package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// IMPORTS DE FIREBASE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    // Variables de estado
    var nametag by remember { mutableStateOf("") } // NUEVO CAMPO
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estados para selects
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var año by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }

    // Estado de carga
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme
    val auth = remember { FirebaseAuth.getInstance() }

    val generos = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")
    val meses = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón Volver
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            TextButton(onClick = onBackToLogin) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver al Login", color = colores.primary)
            }
        }

        Text(text = "Crear Cuenta", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = colores.onBackground)
        Text(text = "Únete a la comunidad de ByteMasters", fontSize = 14.sp, color = colores.onSurfaceVariant)

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN: IDENTIDAD EN LA TIENDA ---
        SectionHeader(text = "Tu Identidad Gamer", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(
            value = nametag,
            onValueChange = { nametag = it },
            label = "Nametag (Nombre de usuario)",
            colores = colores,
            placeholder = "Ej: PlayerOne_99"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: INFORMACIÓN PERSONAL ---
        SectionHeader(text = "Información Personal", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = nombre, onValueChange = { nombre = it }, label = "Nombre(s)", colores = colores)

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomOutlinedField(value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, label = "Apellido", modifier = Modifier.weight(1f), colores = colores)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: FECHA DE NACIMIENTO (Simplificada para el ejemplo) ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomOutlinedField(value = dia, onValueChange = { if (it.length <= 2) dia = it }, label = "Día", modifier = Modifier.weight(1f), colores = colores)
            CustomOutlinedField(value = año, onValueChange = { if (it.length <= 4) año = it }, label = "Año", modifier = Modifier.weight(1f), colores = colores)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: CUENTA ---
        SectionHeader(text = "Seguridad de la Cuenta", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = email, onValueChange = { email = it }, label = "Correo electrónico", colores = colores)
        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedField(value = password, onValueChange = { password = it }, label = "Contraseña", visualTransformation = PasswordVisualTransformation(), colores = colores)
        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar contraseña", visualTransformation = PasswordVisualTransformation(), colores = colores)

        Spacer(modifier = Modifier.height(40.dp))

        // --- LÓGICA DE REGISTRO ---
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && nametag.isNotEmpty()) {
                    if (password == confirmPassword) {
                        if (password.length >= 6) {
                            isLoading = true
                            auth.createUserWithEmailAndPassword(email.trim(), password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser

                                        // 1. Guardar el NAMETAG en el perfil de Firebase
                                        val profileUpdates = userProfileChangeRequest {
                                            displayName = nametag
                                        }

                                        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                                            // 2. Enviar correo de verificación
                                            user.sendEmailVerification().addOnCompleteListener { verifyTask ->
                                                isLoading = false
                                                if (verifyTask.isSuccessful) {
                                                    Toast.makeText(context, "Registro exitoso. ¡Revisa tu correo para verificar tu cuenta!", Toast.LENGTH_LONG).show()
                                                    auth.signOut() // Cerramos sesión hasta que verifique
                                                    onBackToLogin()
                                                } else {
                                                    Toast.makeText(context, "Error al enviar verificación.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("REGISTRARME Y VERIFICAR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Los componentes SectionHeader y CustomOutlinedField se mantienen igual que los tenías...
@Composable
fun SectionHeader(text: String, color: Color) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CustomOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    colores: ColorScheme,
    modifier: Modifier = Modifier,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colores.primary,
            focusedLabelColor = colores.primary,
            cursorColor = colores.primary
        )
    )
}