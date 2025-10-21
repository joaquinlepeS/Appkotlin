package com.example.app_kotlin.utils

import android.util.Patterns
import java.text.SimpleDateFormat
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
    val fechaError: String? = null,
    val horaError: String? = null,
    val especialidadError: String? = null,
    val doctorError: String? = null
)

// --- Validaciones existentes ---
fun validateUsuario(usuario: String): String? {
    return when {
        usuario.isEmpty() -> "El usuario no puede estar vacío"
        usuario.length < 5 -> "El usuario debe tener mas de 4 caracteres"
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
    return when {
        password1 != password2 -> "Las contraseñas no coinciden"
        else -> null
    }
}

fun validateEspecialidad(especialidad:String): String? {
    return when {
        especialidad.isEmpty() -> "La especialidad no puede estar vacía"
        else -> null
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
        } else null
    } catch (e: Exception) {
        "Fecha incorrecta"
    }
}

// --- NUEVAS VALIDACIONES PARA AGENDA SCREEN ---
fun validateHora(hora: String): String? {
    return when {
        hora.isEmpty() -> "La hora no puede estar vacía"
        !hora.matches(Regex("^([01]?\\d|2[0-3]):([0-5]\\d)\$")) -> "Formato de hora inválido (HH:mm)"
        else -> null
    }
}

fun validateDoctor(doctor: String): String? {
    return when {
        doctor.isEmpty() -> "Selecciona un doctor"
        else -> null
    }
}

fun validateAgenda(fecha: String, hora: String, especialidad: String, doctor: String): AgendaErrors {
    return AgendaErrors(
        fechaError = validateFecha(fecha),
        horaError = validateHora(hora),
        especialidadError = validateEspecialidad(especialidad),
        doctorError = validateDoctor(doctor)
    )
}
