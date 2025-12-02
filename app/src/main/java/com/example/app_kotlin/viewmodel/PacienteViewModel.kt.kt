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
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun registrarPaciente(paciente: Paciente) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.registrarPaciente()")
                pacienteActual = repository.createPaciente(paciente)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
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
}
