package com.example.app_kotlin.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object DoctorRetrofit {

    private const val BASE_URL = "https://randomuser.me/"

    // Logging interceptor — muestra TODO el JSON en Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con timeout extendido
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    val api: DoctorApiService by lazy {

        println("DEBUG → INICIALIZANDO RETROFIT DOCTOR...")
        println("DEBUG → Base URL: $BASE_URL")

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DoctorApiService::class.java)
    }
}
