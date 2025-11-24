package com.example.app_kotlin.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultas")
data class ConsultaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorId: Int,
    val pacienteId: Int,
    val fecha: String,
    val hora: String,
    val especialidad: String
)
