package com.example.app_kotlin.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Hospital
import com.example.app_kotlin.repository.HospitalRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf



class HospitalViewModel : ViewModel() {

    private val repo = HospitalRepository()

    var hospitales by mutableStateOf<List<Hospital>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun cargarHospitales() {
        viewModelScope.launch {
            try {
                isLoading = true
                hospitales = repo.obtenerHospitalesSantiago()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
