package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Usuario
import com.example.app_kotlin.model.DataStoreManager
import kotlinx.coroutines.flow.first

class UsuarioRepository(private val dataStore: DataStoreManager) {

    suspend fun registrarUsuario(usuario: Usuario): Boolean {
        val usuarios = dataStore.getUsers().first()

        if (usuarios.any { it.email == usuario.email }) return false

        val lista = usuarios.toMutableList()
        lista.add(usuario)

        dataStore.saveUsers(lista)
        return true
    }

    suspend fun login(email: String, password: String): Usuario? {
        val usuarios = dataStore.getUsers().first()
        return usuarios.find { it.email == email && it.password == password }
    }
}

