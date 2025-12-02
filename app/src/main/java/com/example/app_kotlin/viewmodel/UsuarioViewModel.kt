package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Paciente
import com.example.app_kotlin.repository.UsuarioRepository
import com.example.app_kotlin.model.DataStoreManager
import com.example.app_kotlin.App
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class UsuarioViewModel : ViewModel() {

    // ✔ DataStore global usando Application context
    private val dataStore = DataStoreManager(App.context)

    // ✔ Repositorio interno (NO requiere constructor externo)
    private val repo = UsuarioRepository(dataStore)

    var pacienteActual by mutableStateOf<Paciente?>(null)
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
                pacienteActual = usuario
                loginError = null
            }
        }
    }

    fun registrarUsuario(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            val ok = repo.registrarUsuario(Paciente(email, password, nombre))
            registroExitoso = ok
        }
    }

    fun logout() {
        pacienteActual = null
    }
}
