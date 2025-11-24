package com.example.app_kotlin.model

data class Consulta(
    val id: Int,
    val fecha: String,
    val hora: String,
    val especialidad: String,
    val doctor: String,
    val paciente: String
)
