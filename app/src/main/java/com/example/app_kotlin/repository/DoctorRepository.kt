package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.remote.DoctorRetrofit

class DoctorRepository {

    suspend fun getDoctors(): List<Doctor> {
        return DoctorRetrofit.api.getDoctoresBackend()
    }
}
