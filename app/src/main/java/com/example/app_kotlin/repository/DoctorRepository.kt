package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.remote.RandomUser
import com.example.app_kotlin.remote.RetrofitClient

class DoctorRepository {

    // Lista de especialidades
    private val specialties = listOf(
        "Medicina General",
        "Cardiología",
        "Neurología",
        "Pediatría",
        "Dermatología",
        "Geriatría",
        "Psiquiatría",
        "Gastroenterología"
    )

    // Función para generar años de experiencia
    private fun generateExperience(): Int {
        return (3..35).random()
    }

    // Función para generar especialidad
    private fun generateSpecialty(): String {
        return specialties.random()
    }

    // Función principal que obtiene doctores desde la API
    suspend fun getDoctors(): List<Doctor> {
        val response = RetrofitClient.api.getDoctors(results = 10)

        return response.results.map { user ->
            mapRandomUserToDoctor(user)
        }
    }

    // Mapeo de RandomUser → Doctor
    private fun mapRandomUserToDoctor(user: RandomUser): Doctor {
        return Doctor(
            name = "${user.name.first} ${user.name.last}",
            email = user.email,
            phone = user.phone,
            picture = user.picture.large,
            city = user.location.city,
            country = user.location.country,
            specialty = generateSpecialty(),
            yearsExperience = generateExperience()
        )
    }
}
