package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.repository.ConsultaRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ConsultaViewModel(private val repo: ConsultaRepository) : ViewModel() {

    var consultas by mutableStateOf<List<Consulta>>(emptyList())
        private set

    fun cargarConsultas(usuarioEmail: String) {
        viewModelScope.launch {
            consultas = repo.obtenerConsultas(usuarioEmail)
        }
    }

    fun agregarConsulta(usuarioEmail: String, consulta: Consulta) {
        viewModelScope.launch {
            repo.agregarConsulta(usuarioEmail, consulta)
            cargarConsultas(usuarioEmail)
        }
    }

    fun eliminarConsulta(usuarioEmail: String, id: Int) {
        viewModelScope.launch {
            repo.eliminarConsulta(usuarioEmail, id)
            cargarConsultas(usuarioEmail)
        }
    }
}
