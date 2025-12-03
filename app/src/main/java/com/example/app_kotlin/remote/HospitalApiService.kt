package com.example.app_kotlin.remote


import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalApiService {

    @GET("interpreter")
    suspend fun getHospitales(
        @Query("data") query: String
    ): HospitalResponse
}
