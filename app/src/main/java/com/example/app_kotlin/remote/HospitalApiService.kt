package com.example.app_kotlin.remote


import com.example.app_kotlin.model.HospitalResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalApiService {

    @GET("interpreter")
    suspend fun getHospitales(
        @Query("data") query: String
    ): HospitalResponse
}
