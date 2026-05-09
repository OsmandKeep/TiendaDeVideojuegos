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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    // Variables de estado para los campos
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var año by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme

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
        // Botón de navegación superior
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

        Text(
            text = "Crear Cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colores.onBackground
        )

        Text(
            text = "Completa tus datos para registrarte",
            fontSize = 14.sp,
            color = colores.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN: INFORMACIÓN PERSONAL ---
        SectionHeader(text = "Información Personal", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = nombre, onValueChange = { nombre = it }, label = "Nombre(s)", colores = colores)

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomOutlinedField(value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, label = "Ap. Paterno", modifier = Modifier.weight(1f), colores = colores)
            CustomOutlinedField(value = apellidoMaterno, onValueChange = { apellidoMaterno = it }, label = "Ap. Materno", modifier = Modifier.weight(1f), colores = colores)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: FECHA DE NACIMIENTO ---
        SectionHeader(text = "Fecha de Nacimiento", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomOutlinedField(value = dia, onValueChange = { if (it.length <= 2) dia = it }, label = "Día", modifier = Modifier.weight(0.8f), colores = colores, placeholder = "31")

            var expandedMes by remember { mutableStateOf(false) }
            Box(modifier = Modifier.weight(1.5f)) {
                OutlinedTextField(
                    value = mes,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Mes") },
                    trailingIcon = { IconButton(onClick = { expandedMes = true }) { Text("▼", fontSize = 10.sp, color = colores.primary) } },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colores.primary, focusedLabelColor = colores.primary)
                )
                DropdownMenu(expanded = expandedMes, onDismissRequest = { expandedMes = false }) {
                    meses.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { mes = item; expandedMes = false }) }
                }
            }

            CustomOutlinedField(value = año, onValueChange = { if (it.length <= 4) año = it }, label = "Año", modifier = Modifier.weight(1f), colores = colores, placeholder = "1999")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: GÉNERO ---
        SectionHeader(text = "Género", color = colores.primary)
        var expandedGenero by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedTextField(
                value = genero,
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecciona tu género") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { IconButton(onClick = { expandedGenero = true }) { Text("▼", fontSize = 10.sp, color = colores.primary) } },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colores.primary, focusedLabelColor = colores.primary)
            )
            DropdownMenu(expanded = expandedGenero, onDismissRequest = { expandedGenero = false }, modifier = Modifier.fillMaxWidth(0.8f)) {
                generos.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { genero = item; expandedGenero = false }) }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = colores.surfaceVariant, thickness = 1.dp)
        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN: INFORMACIÓN DE CUENTA ---
        SectionHeader(text = "Información de Cuenta", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = email, onValueChange = { email = it }, label = "Correo electrónico", colores = colores)

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = password, onValueChange = { password = it }, label = "Contraseña", visualTransformation = PasswordVisualTransformation(), colores = colores)

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirmar contraseña", visualTransformation = PasswordVisualTransformation(), colores = colores)

        Spacer(modifier = Modifier.height(40.dp))

        // --- BOTÓN DE REGISTRO ---
        Button(
            onClick = {
                if (nombre.isNotEmpty() && email.isNotEmpty() && password == confirmPassword && password.length >= 6) {
                    Toast.makeText(context, "¡Registro exitoso! Bienvenido $nombre", Toast.LENGTH_LONG).show()
                    onBackToLogin()
                } else {
                    Toast.makeText(context, "Por favor, verifica tus datos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
        ) {
            Text("REGISTRARME", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = colores.secondary)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}


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