package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.repository.DoctorRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class DoctorViewModel : ViewModel() {

    private val repository = DoctorRepository()

    var doctors by mutableStateOf<List<Doctor>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchDoctors() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                doctors = repository.getDoctors()

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
