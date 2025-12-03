package com.example.app_kotlin.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HospitalRetrofit {

    private const val BASE_URL = "https://overpass-api.de/api/"

    val api: HospitalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HospitalApiService::class.java)
    }
}
