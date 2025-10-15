package com.example.app_kotlin.utils

import android.util.Patterns

// ------------------------
// Validar correo
// ------------------------
fun validateEmail(email: String): String? {
    return when {
        email.isEmpty() -> "El correo no puede estar vacío"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo inválido"
        else -> null
    }
}

// ------------------------
// Validar contraseña
// ------------------------
fun validatePassword(password: String): String? {
    return when {
        password.isEmpty() -> "La contraseña no puede estar vacía"
        password.length < 8 -> "La contraseña debe tener mínimo 8 caracteres"
        else -> null
    }
}

// ------------------------
// Validación completa de login (opcional)
// ------------------------
data class LoginErrors(
    val emailError: String? = null,
    val passwordError: String? = null
)

fun validateLogin(email: String, password: String): LoginErrors {
    return LoginErrors(
        emailError = validateEmail(email),
        passwordError = validatePassword(password)
    )
}