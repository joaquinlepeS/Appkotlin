package com.example.app_kotlin.remote

import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.model.RandomUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DoctorApiService {

    // 🔹 GET desde RandomUser
    @GET("api/")
    suspend fun getDoctors(
        @Query("results") results: Int = 10
    ): RandomUserResponse


    // 🔹 POST hacia MockAPI → para guardar doctores
    @POST("doctores")
    suspend fun postDoctor(
        @Body doctor: Doctor
    ): Doctor
}
