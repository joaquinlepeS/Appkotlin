package com.example.app_kotlin.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientConsulta {

    private const val BASE_URL = "https://6923d4823ad095fb8471a99d.mockapi.io/api/v1/consultas/"

    val api: ConsultaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConsultaApiService::class.java)
    }
}
