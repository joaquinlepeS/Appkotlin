package com.example.app_kotlin.remote

import com.example.app_kotlin.model.ConsultaApi
import retrofit2.http.GET
import retrofit2.http.Query

interface ConsultaApiService {

    // 🔹 Obtiene TODAS las consultas de la API
    @GET("consultas")
    suspend fun getConsultas(): List<ConsultaApi>

    // 🔹 Obtiene consultas filtradas por doctor (MockAPI usa ?doctorId=1 por ejemplo)
    @GET("consultas")
    suspend fun getConsultasPorDoctor(
        @Query("doctorId") doctorId: String
    ): List<ConsultaApi>
}
