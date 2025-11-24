package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.remote.RandomUser
import com.example.app_kotlin.remote.RandomUserResponse
import com.example.app_kotlin.remote.RetrofitClientDoctor

class DoctorRepository {

    suspend fun fetchDoctors(): List<Doctor> {
        return try {
            // Usa TU Retrofit real
            val response: RandomUserResponse = RetrofitClientDoctor.api.getDoctors(10)

            response.results.map { user ->
                mapRandomUserToDoctor(user)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun mapRandomUserToDoctor(user: RandomUser): Doctor {
        return Doctor(
            nombre = "${user.name.first} ${user.name.last}",
            email = user.email,
            telefono = user.phone,
            foto = user.picture.large,
            ciudad = user.location.city,
            pais = user.location.country,
            especialidad = generateSpecialty(),
            experiencia = generateExperience()
        )
    }

    private fun generateSpecialty(): String {
        val especialidades = listOf(
            "Cardiología",
            "Dermatología",
            "Pediatría",
            "Ginecología",
            "Neurología",
            "Medicina General",
            "Traumatología",
            "Psiquiatría",
            "Endocrinología",
            "Otorrinolaringología",
            "Urología",
            "Cirugía General",
            "Oncología"
        )
        return especialidades.random()
    }

    private fun generateExperience(): Int {
        return (1..30).random()
    }
}
