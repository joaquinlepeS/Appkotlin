package com.example.app_kotlin.repository

import com.example.app_kotlin.remote.RetrofitClientDoctor
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.model.RandomUser

class DoctorRepository {

    suspend fun getDoctors(): List<Doctor> {
        return try {
            val response = RetrofitClientDoctor.api.getDoctors()

            response.results.map { user ->

                Doctor(
                    nombre = "${user.name.first} ${user.name.last}",
                    email = user.email,
                    telefono = user.phone,                // ✔ ahora sí
                    foto = user.picture.large,
                    ciudad = user.location.city,
                    pais = user.location.country,
                    especialidad = "Medicina General",     // ✔ está bien que sea fijo por ahora
                    experiencia = (3..20).random()         // ✔ simulación
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }
}
