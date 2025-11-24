package com.example.app_kotlin.repository

import com.example.app_kotlin.model.ConsultaApi
import com.example.app_kotlin.remote.RetrofitClientConsulta

class ConsultaApiRepository {

    suspend fun obtenerConsultas(): List<ConsultaApi> {
        return try {
            RetrofitClientConsulta.api.obtenerConsultas()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerConsultasPorDoctor(email: String): List<ConsultaApi> {
        val lista = obtenerConsultas()
        return lista.filter { it.doctorEmail == email }
    }
}
