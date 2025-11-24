package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.repository.DoctorRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class DoctorViewModel : ViewModel() {

    private val repository = DoctorRepository()

    // Estado: lista de doctores
    var doctors by mutableStateOf<List<Doctor>>(emptyList())
        private set

    // Estado: cargando data
    var isLoading by mutableStateOf(false)
        private set

    // Estado: error
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Función que consume el repositorio
    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                doctors = repository.getDoctors()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }
}

