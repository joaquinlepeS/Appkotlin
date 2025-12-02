package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Paciente
import com.example.app_kotlin.remote.PacienteRetrofit

class PacienteRepository {

    suspend fun createPaciente(paciente: Paciente): Paciente {
        println("DEBUG → Repository.createPaciente() llamado")
        return PacienteRetrofit.api.createPaciente(paciente)
    }

    suspend fun getPacienteById(id: Long): Paciente {
        println("DEBUG → Repository.getPacienteById($id) llamado")
        return PacienteRetrofit.api.getPacienteById(id)
    }

    suspend fun getPacienteByEmail(email: String): Paciente {
        println("DEBUG → Repository.getPacienteByEmail($email) llamado")
        return PacienteRetrofit.api.getPacienteByEmail(email)
    }
}
