package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.utils.validateEmail
import com.example.app_kotlin.utils.validatePassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    appState: AppState,
    onNavigateToHome: () -> Unit,
    onNavigateToRegistro: () -> Unit,
    esDoctor: Boolean // true si es login de doctor
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (esDoctor) "Login Doctor" else "Login Usuario",
            style = MaterialTheme.typography.headlineSmall
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = validateEmail(it)
                loginError = null
            },
            label = { Text("Email") },
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (emailError != null) {
            Text(emailError!!, color = MaterialTheme.colorScheme.error)
        }

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = validatePassword(it)
                loginError = null
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError != null) {
            Text(passwordError!!, color = MaterialTheme.colorScheme.error)
        }

        // Botón Login
        Button(
            onClick = {
                emailError = validateEmail(email)
                passwordError = validatePassword(password)

                if (emailError == null && passwordError == null) {
                    val exito = if (esDoctor) {
                        appState.loginDoctor(email.trim().lowercase(), password)
                    } else {
                        appState.loginUser(email.trim().lowercase(), password)
                    }

                    if (exito) {
                        loginError = null
                        onNavigateToHome() // navegación que tenías antes
                    } else {
                        loginError = "Usuario o contraseña incorrecta"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        // Navegar a registro
        TextButton(onClick = { onNavigateToRegistro() }) {
            Text(text = if (esDoctor) "Registrar Doctor" else "Registrar Usuario")
        }

        if (loginError != null) {
            Text(loginError!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
