package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Paciente
import com.example.app_kotlin.repository.PacienteRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class PacienteViewModel : ViewModel() {

    private val repository = PacienteRepository()

    var pacienteActual by mutableStateOf<Paciente?>(null)


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun registrarPaciente(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val nuevo = Paciente(
                    id = null,
                    nombre = nombre,
                    email = email,
                    password = password
                )

                val creado = repository.createPaciente(nuevo)

                pacienteActual = creado

            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun buscarPorId(id: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                pacienteActual = repository.getPacienteById(id)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → Login buscando paciente con email: $email")

                val paciente = repository.getPacienteByEmail(email)

                println("DEBUG → Paciente encontrado: $paciente")

                // Validar contraseña
                if (paciente.password != password) {
                    errorMessage = "Contraseña incorrecta"
                    return@launch
                }

                pacienteActual = paciente
                onSuccess()

            } catch (e: Exception) {
                println("DEBUG → ERROR LOGIN: ${e.message}")
                errorMessage = "Email no encontrado"
            } finally {
                isLoading = false
            }
        }
    }

    fun buscarPorEmail(email: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                pacienteActual = repository.getPacienteByEmail(email)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        pacienteActual = null
    }

}
