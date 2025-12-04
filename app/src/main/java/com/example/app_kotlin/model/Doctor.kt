package com.example.app_kotlin.model

data class Doctor(
    val id:Long? = null,
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val foto: String = "",
    val ciudad: String = "",
    val pais: String = "",
    val especialidad: String = "",
    val experiencia: Int? = null
)
