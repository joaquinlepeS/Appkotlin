package com.example.app_kotlin.remote

import com.example.app_kotlin.model.Consulta
import retrofit2.http.*

interface ConsultaApiService {

    @GET("consultas")
    suspend fun getConsultas(): List<Consulta>

    @GET("consultas/{id}")
    suspend fun getConsultaById(@Path("id") id: Long): Consulta

    @GET("consultas/paciente/{id}")
    suspend fun getConsultasPorPaciente(@Path("id") pacienteId: Long): List<Consulta>

    @GET("consultas/doctor/{id}")
    suspend fun getConsultasPorDoctor(@Path("id") doctorId: Long): List<Consulta>

    @PUT("consultas/{id}")
    suspend fun updateConsulta(
        @Path("id") id: Long,
        @Body consulta: Consulta
    ): Consulta

    @DELETE("consultas/{id}")
    suspend fun deleteConsulta(@Path("id") id: Long)
}
