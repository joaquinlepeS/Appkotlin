package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.remote.RandomUserModels
import com.example.app_kotlin.remote.RandomUser

class DoctorRepository {

    suspend fun fetchDoctors(): List<Doctor> {
        return try {
            val response: RandomUserResponse = RandomUserApi.api.getRandomUsers(10)
            response.results.map { user -> mapRandomUserToDoctor(user) }
        } catch (e: Exception) {
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

    // Genera especialidades al azar para los doctores
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

    // Genera años de experiencia al azar
    private fun generateExperience(): Int {
