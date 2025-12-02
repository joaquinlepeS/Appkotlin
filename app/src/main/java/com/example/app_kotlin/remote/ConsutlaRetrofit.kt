package com.example.app_kotlin.remote

import com.example.app_kotlin.remote.ConsultaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ConsultaRetrofit {

    // BASE URL de tu microservicio
    private const val BASE_URL = "http://10.0.2.2:8080/"  // Emulador → localhost del backend

    // Logging interceptor — muestra todo el JSON en Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con timeout extendido (igual que DoctorRetrofit)
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    val api: ConsultaApiService by lazy {

        println("DEBUG → INICIALIZANDO RETROFIT CONSULTA...")
        println("DEBUG → Base URL: $BASE_URL")

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConsultaApiService::class.java)
    }
}
