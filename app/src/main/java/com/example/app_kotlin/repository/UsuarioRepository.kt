package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Paciente
import com.example.app_kotlin.model.DataStoreManager
import kotlinx.coroutines.flow.first

class UsuarioRepository(private val dataStore: DataStoreManager) {

    suspend fun registrarUsuario(paciente: Paciente): Boolean {
        val usuarios = dataStore.getUsers().first()

        if (usuarios.any { it.email == paciente.email }) return false

        val lista = usuarios.toMutableList()
        lista.add(paciente)

        dataStore.saveUsers(lista)
        return true
    }

    suspend fun login(email: String, password: String): Paciente? {
        val usuarios = dataStore.getUsers().first()
        return usuarios.find { it.email == email && it.password == password }
    }
}

