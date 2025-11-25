package com.example.app_kotlin.remote

import com.example.app_kotlin.model.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DoctorApiService {

    @GET("api/")
    suspend fun getDoctors(
        @Query("results") results: Int = 10
    ): RandomUserResponse
}
