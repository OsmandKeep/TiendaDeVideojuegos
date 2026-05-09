package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) }

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBackToLogin,
                    contentPadding = PaddingValues(start = 0.dp)
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
        }

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Recuperar contraseña",
            modifier = Modifier.size(80.dp),
            tint = colores.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground
        )

        Text(
            text = when (step) {
                1 -> "Te enviaremos un código de verificación"
                2 -> "Ingresa el código que recibiste"
                3 -> "Crea tu nueva contraseña"
                else -> ""
            },
            fontSize = 14.sp,
            color = colores.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        when (step) {
            1 -> {
                // Tarjeta informativa
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colores.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Recuperación de contraseña",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = colores.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ingresa tu correo electrónico y te enviaremos un código de verificación.",
                            fontSize = 13.sp,
                            color = colores.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colores.primary,
                        focusedLabelColor = colores.primary
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = colores.primary)
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (email.contains("@")) {
                            Toast.makeText(context, "Código enviado", Toast.LENGTH_SHORT).show()
                            step = 2
                        } else {
                            Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
                ) {
                    Text("ENVIAR CÓDIGO", fontWeight = FontWeight.Bold)
                }
            }

            2 -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colores.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Verificación", fontWeight = FontWeight.Bold, color = colores.primary)
                        Text("Código enviado a $email", fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = codigo,
                    onValueChange = { if (it.length <= 6) codigo = it },
                    label = { Text("Código de 6 dígitos") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colores.primary)
                )

                TextButton(onClick = { /* Reenviar */ }) {
                    Text("Reenviar código", color = colores.secondary)
                }

                Button(
                    onClick = { if (codigo.length == 6) step = 3 },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
                ) {
                    Text("VERIFICAR")
                }
            }

            3 -> {
                // Uso del color TERCIARIO para indicar éxito en la verificación (como en los descuentos)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colores.tertiaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = colores.tertiary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Identidad confirmada", color = colores.onTertiaryContainer)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = nuevaPassword,
                    onValueChange = { nuevaPassword = it },
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (nuevaPassword == confirmPassword && nuevaPassword.length >= 6) {
                            Toast.makeText(context, "Éxito", Toast.LENGTH_SHORT).show()
                            onBackToLogin()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
                ) {
                    Text("ACTUALIZAR")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Indicador de pasos dinámico con colores del tema
        Row(
            modifier = Modifier.padding(vertical = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val isCurrent = (index + 1) == step
                val isDone = (index + 1) < step

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(if (isCurrent) 12.dp else 8.dp)
                        .background(
                            color = when {
                                isCurrent -> colores.primary
                                isDone -> colores.primary.copy(alpha = 0.4f)
                                else -> colores.onSurfaceVariant.copy(alpha = 0.2f)
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}