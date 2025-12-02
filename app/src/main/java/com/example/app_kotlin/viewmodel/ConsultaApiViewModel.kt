package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ConsultaApiViewModel : ViewModel() {

    private val repository = ConsultaApiRepository()

    var consultas by mutableStateOf<List<ConsultaApi>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun cargarConsultasPorDoctor(email: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null
                consultas = repository.obtenerConsultasPorDoctor(email)
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
