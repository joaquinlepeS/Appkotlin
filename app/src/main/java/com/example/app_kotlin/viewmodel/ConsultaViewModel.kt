package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.repository.ConsultaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConsultaViewModel(
    private val repository: ConsultaRepository
) : ViewModel() {

    private val _consultas = MutableStateFlow<List<Consulta>>(emptyList())
    val consultas: StateFlow<List<Consulta>> = _consultas

    fun cargarConsultas(email: String) {
        viewModelScope.launch {
            _consultas.value = repository.obtenerConsultas(email)
        }
    }

    fun agregarConsulta(email: String, consulta: Consulta) {
        viewModelScope.launch {
            repository.agregarConsulta(email, consulta)
            cargarConsultas(email) // actualiza lista
        }
    }

    fun eliminarConsulta(email: String, id: Int) {
        viewModelScope.launch {
            repository.eliminarConsulta(email, id)
            cargarConsultas(email) // actualiza lista
        }
    }
}
