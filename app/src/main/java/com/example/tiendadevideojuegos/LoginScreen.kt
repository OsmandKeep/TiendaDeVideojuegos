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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.CheckCircle

// Imports de Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseNetworkException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onForgotClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isCaptchaChecked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme
    val auth = remember { FirebaseAuth.getInstance() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.iconotiendavideojuegos_foreground),
            contentDescription = "Logo de la Tienda",
            modifier = Modifier.size(220.dp),
            tint = colores.primary
        )

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = "Iniciar Sesión",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(28.dp))

        // --- CAMPO DE CORREO ---
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = colores.primary)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colores.primary,
                focusedLabelColor = colores.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO DE CONTRASEÑA (Sin sugerencias de texto) ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = colores.primary)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colores.primary,
                focusedLabelColor = colores.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- TARJETA DE CAPTCHA ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colores.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCaptchaChecked,
                    onCheckedChange = { isCaptchaChecked = it },
                    enabled = !isLoading,
                    colors = CheckboxDefaults.colors(checkedColor = colores.primary)
                )
                Text(
                    text = "No soy un robot",
                    modifier = Modifier.weight(1f),
                    color = colores.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verificación",
                    tint = if (isCaptchaChecked) colores.primary else colores.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- BOTÓN DE ACCESO ---
        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty() && isCaptchaChecked) {
                    isLoading = true
                    val emailInput = username.trim()

                    // PASO 1: Comprobar primero si el correo existe en Firebase
                    auth.fetchSignInMethodsForEmail(emailInput)
                        .addOnCompleteListener { fetchTask ->
                            if (fetchTask.isSuccessful) {
                                val signInMethods = fetchTask.result?.signInMethods

                                if (signInMethods.isNullOrEmpty()) {
                                    // El correo no está registrado en el sistema
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "No se encontró ningún usuario con este correo",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    // PASO 2: El correo existe, ahora validamos contraseña
                                    auth.signInWithEmailAndPassword(emailInput, password)
                                        .addOnCompleteListener { loginTask ->
                                            if (loginTask.isSuccessful) {
                                                val usuarioActual = auth.currentUser

                                                // PASO 3: Validación del enlace de verificación
                                                if (usuarioActual != null && usuarioActual.isEmailVerified) {
                                                    isLoading = false
                                                    Toast.makeText(context, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
                                                    onLoginClick()
                                                } else {
                                                    // Contraseña correcta pero no verificado
                                                    usuarioActual?.sendEmailVerification()
                                                    auth.signOut()
                                                    isLoading = false
                                                    Toast.makeText(
                                                        context,
                                                        "Por favor, confirma tu correo electrónico antes de iniciar sesión. Te hemos enviado un enlace de activación.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            } else {
                                                isLoading = false
                                                // Si el correo existía pero falló el login, la contraseña está mal
                                                val mensajePersonalizado = when (loginTask.exception) {
                                                    is FirebaseNetworkException -> {
                                                        "No hay conexión a internet. Verifica tu red"
                                                    }
                                                    else -> {
                                                        "La contraseña introducida es incorrecta"
                                                    }
                                                }
                                                Toast.makeText(context, mensajePersonalizado, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                }
                            } else {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Error al verificar la cuenta: ${fetchTask.exception?.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    val errorMsg = when {
                        username.isEmpty() -> "Ingresa tu correo"
                        password.isEmpty() -> "Ingresa tu contraseña"
                        !isCaptchaChecked -> "Confirma que no eres un robot"
                        else -> "Datos incompletos"
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
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
                Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ENLACES DE NAVEGACIÓN ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = onRegisterClick, enabled = !isLoading) {
                Text("Crear cuenta", color = colores.primary)
            }

            TextButton(onClick = onForgotClick, enabled = !isLoading) {
                Text("¿Olvidaste tu contraseña?", color = colores.secondary)
            }
        }
    }
}