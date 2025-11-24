package com.example.app_kotlin.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctores")
data class DoctorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val especialidad: String,
    val fotoUrl: String
)
