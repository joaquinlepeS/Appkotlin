package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Hospital
import com.example.app_kotlin.repository.HospitalRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class HospitalViewModel : ViewModel() {

    private val repository = HospitalRepository()

    var hospitales by mutableStateOf<List<Hospital>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun buscarHospitales(ciudad: String) {
        viewModelScope.launch {
            try {
                loading = true
                errorMessage = null
                hospitales = repository.buscarPorCiudad(ciudad)

                if (hospitales.isEmpty()) {
                    errorMessage = "No se encontraron hospitales en '$ciudad'"
                }

            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                loading = false
            }
        }
    }
}
