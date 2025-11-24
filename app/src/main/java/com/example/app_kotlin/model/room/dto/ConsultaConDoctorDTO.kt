package com.example.app_kotlin.room.dto

data class ConsultaConDoctorDTO(
    val id: Int,
    val fecha: String,
    val hora: String,
    val especialidad: String,
    val doctorNombre: String,
    val doctorEmail: String
)
