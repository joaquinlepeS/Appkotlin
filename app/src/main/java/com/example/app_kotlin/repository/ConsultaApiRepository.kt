package com.example.app_kotlin.repository

import com.example.app_kotlin.model.ConsultaApi
import com.example.app_kotlin.remote.RetrofitClientConsulta

class ConsultaApiRepository {

    suspend fun getAllConsultas(): List<ConsultaApi> {
        return try {
            RetrofitClientConsulta.api.getConsultas()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getConsultasPorDoctor(doctorId: String): List<ConsultaApi> {
        return try {
            RetrofitClientConsulta.api.getConsultasPorDoctor(doctorId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
