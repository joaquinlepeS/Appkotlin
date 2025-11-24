package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Usuario
import com.example.app_kotlin.repository.UsuarioRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class UsuarioViewModel(private val repo: UsuarioRepository) : ViewModel() {

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    var registroExitoso by mutableStateOf(false)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val usuario = repo.login(email, password)
            if (usuario == null) {
                loginError = "Credenciales inválidas"
            } else {
                usuarioActual = usuario
                loginError = null
            }
        }
    }

    fun registrarUsuario(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            val ok = repo.registrarUsuario(Usuario(email, password, nombre))
            registroExitoso = ok
        }
    }

    fun logout() {
        usuarioActual = null
    }
}
