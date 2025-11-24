package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Paciente
import com.example.app_kotlin.repository.PacienteRepository
import com.example.app_kotlin.model.DataStoreManager
import com.example.app_kotlin.App
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class PacienteViewModel : ViewModel() {

    // ✔ DataStore global usando Application context
    private val dataStore = DataStoreManager(App.context)

    // ✔ Repositorio interno (NO requiere constructor externo)
    private val repo = PacienteRepository(dataStore)

    var usuarioActual by mutableStateOf<Paciente?>(null)
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
            val ok = repo.registrarUsuario(Paciente(email, password, nombre))
            registroExitoso = ok
        }
    }

    fun logout() {
        usuarioActual = null
    }
}
