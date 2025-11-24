package com.example.app_kotlin.remote

import com.example.app_kotlin.model.ConsultaApi
import retrofit2.http.GET

interface ConsultaApiService {

    @GET("consultas/consultas")
    suspend fun obtenerConsultas(): List<ConsultaApi>
}
