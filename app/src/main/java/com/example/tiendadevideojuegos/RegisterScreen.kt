package com.example.tiendadevideojuegos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable // <--- IMPORTADO PARA SOBREVIVIR A CAMBIOS DE TEMA
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    // Variables de estado cambiadas a rememberSaveable
    var nametag by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellidoPaterno by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    // Estados de visibilidad de contraseñas salvables
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    // Estados para selects salvables
    var dia by rememberSaveable { mutableStateOf("") }
    var mes by rememberSaveable { mutableStateOf("") }
    var año by rememberSaveable { mutableStateOf("") }
    var genero by rememberSaveable { mutableStateOf("") }

    // Estado de carga salvable
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

        // --- SECCIÓN: FECHA DE NACIMIENTO ---
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

        // Campo Contraseña con Botón de Ojo y sin Sugerencias
        CustomOutlinedField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colores = colores,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrectEnabled = false),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Control de visibilidad",
                        tint = colores.primary
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo Confirmar Contraseña con Botón de Ojo y sin Sugerencias
        CustomOutlinedField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar contraseña",
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colores = colores,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrectEnabled = false),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Control de visibilidad",
                        tint = colores.primary
                    )
                }
            }
        )

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

                                        val profileUpdates = userProfileChangeRequest {
                                            displayName = nametag
                                        }

                                        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                                            user.sendEmailVerification().addOnCompleteListener { verifyTask ->
                                                isLoading = false
                                                if (verifyTask.isSuccessful) {
                                                    Toast.makeText(context, "Registro exitoso. ¡Revisa tu correo para verificar tu cuenta!", Toast.LENGTH_LONG).show()
                                                    auth.signOut()
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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
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
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colores.primary,
            focusedLabelColor = colores.primary,
            cursorColor = colores.primary
        )
    )
}

//Hola alfres como estas