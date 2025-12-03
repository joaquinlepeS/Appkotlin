package com.example.app_kotlin.model


data class HospitalResponse(
    val elements: List<OverpassElement>
)

data class OverpassElement(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: OverpassTags?
)

data class OverpassTags(
    val name: String? = null,
    val phone: String? = null,
    val website: String? = null
)
