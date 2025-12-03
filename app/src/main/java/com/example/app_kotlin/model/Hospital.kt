package com.example.app_kotlin.model


data class Hospital(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String?,
    val phone: String? = null
)
