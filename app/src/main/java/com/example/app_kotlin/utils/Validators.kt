package com.example.app_kotlin.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale


data class LoginErrors(
    val usuarioError: String? = null,
    val emailError: String? = null,
    val password1Error: String? = null,
    val password2Error: String? = null,
    val especialidadError: String? = null
)

data class AgendaErrors(
    val fechaError: String? =null
)

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

fun validateEspecialidad(especialidad:String):String?
{   return when {
    especialidad.isEmpty() -> "La especialidad no puede estar vacía"
    else-> null
}
}

fun validateRegistro(usuario: String,email: String, password1: String,password2: String,especialidad:String):LoginErrors{
    return LoginErrors(
        usuarioError = validateUsuario(usuario),
        emailError = validateEmail(email),
        password1Error = validatePassword(password1),
        password2Error = validateRepetirPassword(password1,password2 ),
        especialidadError = validateEspecialidad(especialidad)
    )
}

fun validateFecha(fecha: String): String? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.isLenient = false
        val fechaIngresada = formatter.parse(fecha) ?: return "Fecha incorrecta"

        val hoy = Calendar.getInstance().time

        if (fechaIngresada.before(hoy)) {
            "La fecha no puede ser anterior a hoy"
        } else {
            null // todo ok
        }
    } catch (e: Exception) {
        "Fecha incorrecta"
    }
}