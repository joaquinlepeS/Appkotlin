package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Hospital
import com.example.app_kotlin.remote.HospitalRetrofit

class HospitalRepository {

    suspend fun obtenerHospitalesSantiago(): List<Hospital> {
        val query = """
            [out:json];
            node["amenity"="hospital"](around:5000,-33.45,-70.66);
            out;
        """.trimIndent()

        val res = HospitalRetrofit.api.getHospitales(query)

        return res.elements.map {
            Hospital(
                id = it.id,
                lat = it.lat,
                lon = it.lon,
                name = it.tags?.name
            )
        }
    }
}
