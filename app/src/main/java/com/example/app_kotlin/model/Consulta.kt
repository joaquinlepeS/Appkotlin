package com.example.app_kotlin.model

data class ConsultaApi(
    val id: Int,
    val doctorEmail: String,
    val doctorNombre: String,
    val especialidad: String,
    val fecha: String,
    val hora: String
)
