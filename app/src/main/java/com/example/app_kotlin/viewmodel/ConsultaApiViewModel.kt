package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.ConsultaApi
import com.example.app_kotlin.repository.ConsultaApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConsultaApiViewModel : ViewModel() {

    private val repository = ConsultaApiRepository()

    private val _consultas = MutableStateFlow<List<ConsultaApi>>(emptyList())
    val consultas: StateFlow<List<ConsultaApi>> = _consultas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    /** 🔵 Obtener TODAS las consultas de la API */
    fun cargarTodasLasConsultas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _consultas.value = repository.getAllConsultas()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** 🔵 Obtener consultas SOLO para un doctor */
    fun cargarConsultasPorDoctor(doctorId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _consultas.value = repository.getConsultasPorDoctor(doctorId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
