package com.example.app_kotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.repository.ConsultaRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class ConsultaViewModel : ViewModel() {

    // Repositorio para acceder a Retrofit
    private val repository = ConsultaRepository()

    // Estado: lista de consultas (para listas)
    var consultas by mutableStateOf<List<Consulta>>(emptyList())
        private set

    // Estado: una consulta (para detalle, editar, actualizar)
    var consultaSeleccionada by mutableStateOf<Consulta?>(null)
        private set

    // Estado: cargando
    var isLoading by mutableStateOf(false)
        private set

    // Estado: error
    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun loadAll() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.loadAll()")
                consultas = repository.getAll()

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadByDoctor(doctorId: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.loadByDoctor($doctorId)")
                consultas = repository.getByDoctor(doctorId)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadByPaciente(pacienteId: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.loadByPaciente($pacienteId)")
                consultas = repository.getByPaciente(pacienteId)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadById(id: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.loadById($id)")
                consultaSeleccionada = repository.getById(id)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateConsulta(id: Long, consulta: Consulta) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.updateConsulta($id)")
                consultaSeleccionada = repository.update(id, consulta)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteConsulta(id: Long) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                println("DEBUG → ViewModel.deleteConsulta($id)")
                repository.delete(id)

                // Después de eliminar, recargar lista
                loadAll()

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun liberarConsulta(consulta: Consulta) {
        viewModelScope.launch {
            try {
                isLoading = true

                val consultaLiberada = consulta.copy(paciente = null)

                consultaSeleccionada = repository.update(
                    id = consulta.id!!,
                    consulta = consultaLiberada
                )

                // Recargar lista para que UI se actualice
                loadByPaciente(consulta.paciente?.id ?: -1)

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error al liberar consulta"
            } finally {
                isLoading = false
            }
        }
    }

}
