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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var nametag by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellidoPaterno by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var dia by rememberSaveable { mutableStateOf("") }
    var mes by rememberSaveable { mutableStateOf("") }
    var año by rememberSaveable { mutableStateOf("") }

    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val colores = MaterialTheme.colorScheme
    val auth = remember { FirebaseAuth.getInstance() }

    val calendarioActual = Calendar.getInstance()
    val añoActual = calendarioActual.get(Calendar.YEAR)
    val mesActual = calendarioActual.get(Calendar.MONTH) + 1
    val diaActual = calendarioActual.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        SectionHeader(text = "Información Personal", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(
            value = nombre,
            onValueChange = { input ->
                if (input.all { it.isLetter() || it.isWhitespace() }) {
                    nombre = input
                }
            },
            label = "Nombre(s)",
            colores = colores
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(
            value = apellidoPaterno,
            onValueChange = { input ->
                if (input.all { it.isLetter() || it.isWhitespace() }) {
                    apellidoPaterno = input
                }
            },
            label = "Apellido Paterno",
            colores = colores
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CustomOutlinedField(
                value = dia,
                onValueChange = { input ->
                    if (input.length <= 2 && input.all { it.isDigit() }) {
                        val num = input.toIntOrNull()
                        if (num == null || num in 1..31) {
                            dia = input
                        }
                    }
                },
                label = "Día",
                modifier = Modifier.weight(1f),
                colores = colores,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            CustomOutlinedField(
                value = mes,
                onValueChange = { input ->
                    if (input.length <= 2 && input.all { it.isDigit() }) {
                        val num = input.toIntOrNull()
                        if (num == null || num in 1..12) {
                            mes = input
                        }
                    }
                },
                label = "Mes",
                modifier = Modifier.weight(1f),
                colores = colores,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            CustomOutlinedField(
                value = año,
                onValueChange = { input ->
                    if (input.length <= 4 && input.all { it.isDigit() }) {
                        año = input
                    }
                },
                label = "Año",
                modifier = Modifier.weight(1.2f),
                colores = colores,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(text = "Seguridad de la Cuenta", color = colores.primary)
        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(value = email, onValueChange = { email = it }, label = "Correo electrónico", colores = colores, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutlinedField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña (Mín. 8 caracteres)",
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colores = colores,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false),
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

        CustomOutlinedField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar contraseña",
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colores = colores,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false),
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

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && nametag.isNotEmpty() && nombre.isNotEmpty() && apellidoPaterno.isNotEmpty() && dia.isNotEmpty() && mes.isNotEmpty() && año.isNotEmpty()) {

                    val d = dia.toInt()
                    val m = mes.toInt()
                    val a = año.toInt()

                    if (a < (añoActual - 120) || a > añoActual) {
                        Toast.makeText(context, "Por favor introduce un año válido coherente.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val maximosDiasDelMes = when (m) {
                        2 -> if ((a % 4 == 0 && a % 100 != 0) || (a % 400 == 0)) 29 else 28
                        4, 6, 9, 11 -> 30
                        else -> 31
                    }

                    if (d > maximosDiasDelMes) {
                        Toast.makeText(context, "El mes seleccionado solo tiene hasta $maximosDiasDelMes días.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (a == añoActual) {
                        if (m > mesActual || (m == mesActual && d > diaActual)) {
                            Toast.makeText(context, "La fecha de nacimiento no puede ser una fecha futura.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    }

                    if (password == confirmPassword) {
                        if (password.length >= 8) {
                            isLoading = true
                            auth.createUserWithEmailAndPassword(email.trim(), password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        val uid = user?.uid ?: ""

                                        val datosUsuario = hashMapOf(
                                            "id" to uid,
                                            "nombre" to nombre.trim(),
                                            "apellido" to apellidoPaterno.trim(),
                                            "nametag" to nametag.trim(),
                                            "fechaNacimiento" to "$d/$m/$a",
                                            "deseados" to emptyList<String>(),
                                            "carrito" to emptyList<String>(),
                                            "biblioteca" to emptyList<String>()
                                        )

                                        val db = FirebaseFirestore.getInstance()
                                        db.collection("usuarios").document(uid)
                                            .set(datosUsuario)
                                            .addOnCompleteListener { firestoreTask ->
                                                if (firestoreTask.isSuccessful) {
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
                                                    Toast.makeText(context, "Error al crear perfil en la BD: ${firestoreTask.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    } else {
                                        isLoading = false
                                        Toast.makeText(context, "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Por favor llena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
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
