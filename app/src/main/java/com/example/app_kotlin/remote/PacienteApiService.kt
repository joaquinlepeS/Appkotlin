package com.example.app_kotlin.remote

import com.example.app_kotlin.model.Paciente
import retrofit2.http.*

interface PacienteApiService {

    @POST("pacientes")
    suspend fun createPaciente(@Body paciente: Paciente): Paciente

    @GET("pacientes/{id}")
    suspend fun getPacienteById(@Path("id") id: Long): Paciente

    // Opcional → buscar paciente por email
    @GET("pacientes/email/{email}")
    suspend fun getPacienteByEmail(@Path("email") email: String): Paciente
}
