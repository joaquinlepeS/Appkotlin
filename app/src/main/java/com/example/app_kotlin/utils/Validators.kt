package com.example.app_kotlin.utils

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Locale

// -----------------------------
// 🔹 DATA CLASSES DE ERRORES
// -----------------------------
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

// -----------------------------
// 🔹 VALIDACIONES GENERALES
// -----------------------------
fun validateUsuario(usuario: String): String? {
    return when {
        usuario.isEmpty() -> "El usuario no puede estar vacío"
        usuario.length < 5 -> "El usuario debe tener más de 4 caracteres"
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

fun validateRepetirPassword(password1: String, password2: String): String? {
    return when {
        password1 != password2 -> "Las contraseñas no coinciden"
        else -> null
    }
}

fun validateEspecialidad(especialidad: String): String? {
    return when {
        especialidad.isEmpty() -> "La especialidad no puede estar vacía"
        else -> null
    }
}

// -----------------------------
// 🔹 VALIDACIÓN DE REGISTRO
// -----------------------------
fun validateRegistro(
    usuario: String,
    email: String,
    password1: String,
    password2: String,
    especialidad: String
): LoginErrors {
    return LoginErrors(
        usuarioError = validateUsuario(usuario),
        emailError = validateEmail(email),
        password1Error = validatePassword(password1),
        password2Error = validateRepetirPassword(password1, password2),
        especialidadError = validateEspecialidad(especialidad)
    )
}

// -----------------------------
// 🔹 VALIDACIONES DE AGENDA
// -----------------------------

@RequiresApi(Build.VERSION_CODES.O)
fun validateFecha(fecha: String): String? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = LocalDate.parse(fecha, formatter)
        val today = LocalDate.now()
        when {
            fecha.isEmpty() -> "La fecha no puede estar vacía"
            parsedDate.isBefore(today) -> "La fecha no puede ser anterior a hoy"
            else -> null
        }
    } catch (e: DateTimeParseException) {
        "Formato inválido (usa YYYY-MM-DD)"
    }
}

fun validateHora(hora: String): String? {
    val regex = Regex("^([01]?\\d|2[0-3]):[0-5]\\d$")
    return when {
        hora.isEmpty() -> "La hora no puede estar vacía"
        !regex.matches(hora) -> "Formato inválido (usa HH:mm)"
        else -> null
    }
}

fun validateEspecialidadAgenda(especialidad: String): String? {
    return if (especialidad.isEmpty()) "Debe seleccionar una especialidad" else null
}

fun validateDoctor(doctor: String): String? {
    return if (doctor.isEmpty()) "Debe seleccionar un doctor" else null
}

@RequiresApi(Build.VERSION_CODES.O)
fun validateAgenda(
    fecha: String,
    hora: String,
    especialidad: String,
    doctor: String
): AgendaErrors {
    return AgendaErrors(
        fechaError = validateFecha(fecha),
        horaError = validateHora(hora),
        especialidadError = validateEspecialidadAgenda(especialidad),
        doctorError = validateDoctor(doctor)
    )
}

fun validateLogin(email: String, password: String): LoginErrors {
    return LoginErrors(
        emailError = validateEmail(email),
        password1Error = validatePassword(password)
    )
}

