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
import com.example.app_kotlin.utils.validateLogin
import com.example.app_kotlin.utils.validatePassword
import com.example.app_kotlin.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToConsultaCliente: () -> Unit
) {
    // ViewModel nuevo basado en MVVM
    val usuarioViewModel: UsuarioViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val loginError = usuarioViewModel.loginError

    // Si el usuario inició sesión correctamente, navegar
    LaunchedEffect(usuarioViewModel.usuarioActual) {
        if (usuarioViewModel.usuarioActual != null) {
            onNavigateToConsultaCliente()
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_medical_services_24),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = "ConsultaMed",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
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
                    border = BorderStroke(16.dp, Color.Transparent),
                    elevation = CardDefaults.cardElevation(36.dp),
                    modifier = Modifier.padding(16.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Inicia Sesión",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )

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
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

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
                            modifier = Modifier.padding(3.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
                            )
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

                        // BOTÓN INGRESAR
                        Button(
                            onClick = {
                                val errors = validateLogin(email, password)
                                emailError = errors.emailError
                                passwordError = errors.password1Error

                                if (errors.emailError == null && errors.password1Error == null) {
                                    usuarioViewModel.login(email, password)
                                }
                            },
                            modifier = Modifier.padding(6.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                "Ingresar",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // ERROR LOGIN
                        if (loginError != null) {
                            Text(
                                text = loginError,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        // REGISTRARSE
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Top

                        ) {
                            Text(
                                text = "¿No tienes una cuenta?",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            TextButton(onClick = { onNavigateToRegistro() }) {
                                Text(
                                    text = "Regístrate",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onNavigateToRegistro = {},
        onNavigateToConsultaCliente = {}
    )
}