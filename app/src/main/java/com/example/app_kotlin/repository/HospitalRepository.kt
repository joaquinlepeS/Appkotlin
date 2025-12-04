package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Hospital
import com.example.app_kotlin.remote.HospitalRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HospitalRepository {

    /**
     * Busca hospitales usando Overpass basado en el nombre de la ciudad.
     */
    suspend fun buscarPorCiudad(ciudad: String): List<Hospital> =
        withContext(Dispatchers.IO) {

            val query = """
                [out:json];
                area["name"="$ciudad"]->.searchArea;
                node["amenity"="hospital"](area.searchArea);
                out center;
            """.trimIndent()

            val response = HospitalRetrofit.api.getHospitales(query)
            val jsonString = response.string()

            parseHospitals(jsonString)
        }


    /**
     * Convierte el JSON de Overpass a la lista de Hospital()
     */
    private fun parseHospitals(json: String): List<Hospital> {
        val jsonObj = JSONObject(json)
        val elements = jsonObj.getJSONArray("elements")

        val result = mutableListOf<Hospital>()

        for (i in 0 until elements.length()) {
            val item = elements.getJSONObject(i)

            val id = item.getLong("id")
            val lat = item.getDouble("lat")
            val lon = item.getDouble("lon")

            val tags = item.optJSONObject("tags")

            val name = tags?.optString("name", "Sin nombre")
            val phone = tags?.optString("phone", "Sin teléfono")

            result.add(
                Hospital(
                    id = id,
                    lat = lat,
                    lon = lon,
                    name = name,
                    phone = phone
                )
            )
        }

        return result
    }
}
