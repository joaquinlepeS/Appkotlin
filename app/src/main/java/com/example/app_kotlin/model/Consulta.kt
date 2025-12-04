package com.example.app_kotlin.model

data class Consulta(
    val id: Long? = null,
    val fecha: String = "",
    val hora: String = "",
    val especialidad: String = "",
    val doctor: Doctor? = null,      // puede llegar completo o solo id dependiendo del backend
    val paciente: Paciente? = null  // normalmente null en este stage
)