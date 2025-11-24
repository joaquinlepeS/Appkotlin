package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.model.DataStoreManager
import kotlinx.coroutines.flow.first

class ConsultaRepository(private val dataStore: DataStoreManager) {

    suspend fun agregarConsulta(usuarioEmail: String, consulta: Consulta) {
        val mapa = dataStore.getConsulta().first().toMutableMap()
        val lista = mapa.getOrPut(usuarioEmail) { mutableListOf() }

        val id = if (lista.isEmpty()) 1 else lista.maxOf { it.id } + 1

        lista.add(consulta.copy(id = id))

        dataStore.saveConsulta(mapa)
    }

    suspend fun obtenerConsultas(usuarioEmail: String): List<Consulta> {
        val mapa = dataStore.getConsulta().first()
        return mapa[usuarioEmail] ?: emptyList()
    }

    suspend fun eliminarConsulta(usuarioEmail: String, id: Int) {
        val mapa = dataStore.getConsulta().first().toMutableMap()
        val lista = mapa[usuarioEmail]?.toMutableList() ?: return

        lista.removeIf { it.id == id }
        mapa[usuarioEmail] = lista

        dataStore.saveConsulta(mapa)
    }
}
