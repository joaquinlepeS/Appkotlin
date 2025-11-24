package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.R
import com.example.app_kotlin.utils.validateEmail
import com.example.app_kotlin.utils.validatePassword
import com.example.app_kotlin.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onNavigateToLogin: () -> Unit
) {
    val usuarioViewModel: UsuarioViewModel = viewModel()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var nombreError by remember { mutableStateOf<String?>(null) }

    // Si el registro fue exitoso → volver a login
    LaunchedEffect(usuarioViewModel.registroExitoso) {
        if (usuarioViewModel.registroExitoso) {
            onNavigateToLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Crear Cuenta",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    border = BorderStroke(12.dp, Color.Transparent),
                    elevation = CardDefaults.cardElevation(24.dp),
                    modifier = Modifier.padding(16.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Crear Cuenta",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        // NOMBRE
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {
                                nombre = it
                                nombreError = if (nombre.length < 3) "Nombre muy corto" else null
                            },
                            label = { Text("Nombre completo") },
                            singleLine = true,
                            modifier = Modifier.padding(3.dp),
                        )
                        if (nombreError != null) {
                            Text(
                                text = nombreError!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // EMAIL
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = validateEmail(email)
                            },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.padding(3.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        )
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // PASSWORD
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordError = validatePassword(password)
                            },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.padding(3.dp)
                        )
                        if (passwordError != null) {
                            Text(
                                text = passwordError!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // BOTÓN REGISTRARSE
                        Button(
                            onClick = {
                                if (
                                    nombreError == null &&
                                    emailError == null &&
                                    passwordError == null &&
                                    nombre.isNotBlank() &&
                                    email.isNotBlank() &&
                                    password.isNotBlank()
                                ) {
                                    usuarioViewModel.registrarUsuario(nombre, email, password)
                                }
                            },
                            modifier = Modifier.padding(6.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text("Registrarse", color = MaterialTheme.colorScheme.onSurface)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(onClick = { onNavigateToLogin() }) {
                            Text(
                                text = "¿Ya tienes cuenta?",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(onNavigateToLogin = {})
}
