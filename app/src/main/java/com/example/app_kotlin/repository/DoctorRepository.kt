package com.example.app_kotlin.repository

import com.example.app_kotlin.remote.RetrofitClientDoctor
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.model.RandomUser

class DoctorRepository {

    // Lista de especialidades reales
    private val especialidadesPosibles = listOf(
        "Medicina General",
        "Pediatría",
        "Cardiología",
        "Dermatología",
        "Ginecología",
        "Traumatología",
        "Neurología",
        "Psiquiatría",
        "Endocrinología",
        "Oftalmología",
        "Odontología",
        "Oncología",
        "Gastroenterología",
        "Nefrología"
    )

    suspend fun getDoctors(): List<Doctor> {
        return try {
            val response = RetrofitClientDoctor.api.getDoctors()

            response.results.map { user ->

                Doctor(
                    nombre = "${user.name.first} ${user.name.last}",
                    email = user.email,
                    telefono = user.phone,
                    foto = user.picture.large,
                    ciudad = user.location.city,
                    pais = user.location.country,

                    // 🎯 antes era fijo, ahora ESPECIALIDAD REAL ALEATORIA
                    especialidad = especialidadesPosibles.random(),

                    experiencia = (3..20).random()
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }
}
