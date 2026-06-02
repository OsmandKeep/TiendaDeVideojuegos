package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable // <--- IMPORTADO PARA SOBREVIVIR A CAMBIOS DE TEMA
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock

// Imports de Firebase necesarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseNetworkException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {
    // Cambiados a rememberSaveable para que el correo y el estado de carga no se pierdan al cambiar de tema
    var email by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme
    val auth = remember { FirebaseAuth.getInstance() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BOTÓN VOLVER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBackToLogin,
                contentPadding = PaddingValues(start = 0.dp),
                enabled = !isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = colores.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Volver al Login",
                    fontSize = 14.sp,
                    color = colores.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- ICONO PRINCIPAL ---
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Recuperar contraseña",
            modifier = Modifier.size(90.dp),
            tint = colores.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- TÍTULOS ---
        Text(
            text = "¿Olvidaste tu contraseña?",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Te enviaremos un enlace seguro a tu correo electrónico para que puedas restablecerla.",
            fontSize = 14.sp,
            color = colores.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
            onTextLayout = {}
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- TARJETA INFORMATIVA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = colores.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Restablecimiento por Correo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colores.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Asegúrate de ingresar el correo con el que registraste tu cuenta en la tienda.",
                    fontSize = 13.sp,
                    color = colores.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- CAMPO DE TEXTO: CORREO ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colores.primary,
                focusedLabelColor = colores.primary
            ),
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = colores.primary)
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- BOTÓN DE ENVIAR ACCIÓN ---
        Button(
            onClick = {
                val emailInput = email.trim()

                if (emailInput.isNotEmpty() && emailInput.contains("@")) {
                    isLoading = true

                    // Lógica nativa de Firebase para reestablecer contraseñas
                    auth.sendPasswordResetEmail(emailInput)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Enlace enviado con éxito. ¡Revisa tu correo electrónico!",
                                    Toast.LENGTH_LONG
                                ).show()
                                // Devolvemos al usuario a la pantalla de login
                                onBackToLogin()
                            } else {
                                val exception = task.exception
                                val mensajeError = when {
                                    exception is FirebaseNetworkException -> {
                                        "Sin conexión a internet. Verifica tu red."
                                    }
                                    exception?.message?.contains("USER_NOT_FOUND", ignoreCase = true) == true -> {
                                        "No existe ningún usuario registrado con este correo."
                                    }
                                    else -> {
                                        "Error: ${exception?.localizedMessage}"
                                    }
                                }
                                Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = colores.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "RESTABLECER CONTRASEÑA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}