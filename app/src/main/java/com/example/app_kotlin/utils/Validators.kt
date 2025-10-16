package com.example.app_kotlin.utils

import android.util.Patterns


fun validateUsuario(usuario: String): String? {
    return when {
        usuario.isEmpty() -> "El usuario no puede estar vacío"
        usuario.length<5 -> "El usuario debe tener mas de 4 caracteres"
        else -> null
    }
}
fun validateEmail(email: String): String? {
    return when {
        email.isEmpty() -> "El correo no puede estar vacío"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo inválido"
        else -> null
    }
}


fun validatePassword(password: String): String? {
    return when {
        password.isEmpty() -> "La contraseña no puede estar vacía"
        password.length < 8 -> "La contraseña debe tener mínimo 8 caracteres"
        else -> null
    }
}


data class LoginErrors(
    val usuarioError: String? = null,
    val emailError: String? = null,
    val password1Error: String? = null,
    val password2Error: String? = null
)

fun validateLogin(email: String, password: String): LoginErrors {
    return LoginErrors(
        emailError = validateEmail(email),
        password1Error = validatePassword(password)
    )
}

fun validateRepetirPassword(password1: String,password2: String): String? {
    return when{
        password1 != (password2)->"Las contraseñas no coinciden"
        else->null
    }
}

fun validateRegistro(usuario: String,email: String, password1: String,password2: String):LoginErrors{
    return LoginErrors(
        usuarioError = validateUsuario(usuario),
        emailError = validateEmail(email),
        password1Error = validatePassword(password1),
        password2Error = validateRepetirPassword(password1,password2 )
    )
}