package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource // Necesario para cargar el XML
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.CheckCircle

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

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme

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

        // Campo de Usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario o Correo") },
            modifier = Modifier.fillMaxWidth(),
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

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = colores.primary)
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colores.primary,
                focusedLabelColor = colores.primary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Captcha
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

        // Botón de acceso
        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty() && isCaptchaChecked) {
                    onLoginClick()
                } else {
                    val errorMsg = when {
                        username.isEmpty() -> "Ingresa tu usuario"
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
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
        ) {
            Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlaces de navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = onRegisterClick) {
                Text("Crear cuenta", color = colores.primary)
            }

            TextButton(onClick = onForgotClick) {
                Text("¿Olvidaste tu contraseña?", color = colores.secondary)
            }
        }
    }
}