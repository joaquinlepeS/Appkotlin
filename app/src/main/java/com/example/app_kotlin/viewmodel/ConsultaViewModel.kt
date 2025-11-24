package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.model.DataStoreManager
import com.example.app_kotlin.repository.ConsultaRepository
import com.example.app_kotlin.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConsultaViewModel : ViewModel() {

    // ✔ DataStoreManager usando el contexto global de la App
    private val dataStoreManager = DataStoreManager(App.context)

    // ✔ Repositorio sin parámetros externos
    private val repository = ConsultaRepository(dataStoreManager)

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
            cargarConsultas(email)
        }
    }

    fun eliminarConsulta(email: String, id: Int) {
        viewModelScope.launch {
            repository.eliminarConsulta(email, id)
            cargarConsultas(email)
        }
    }
}
