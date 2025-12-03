package com.example.app_kotlin.remote

import com.example.app_kotlin.model.Doctor
import retrofit2.http.GET

interface DoctorApiService {

    @GET("doctores")
    suspend fun getDoctoresBackend(): List<Doctor>

}
