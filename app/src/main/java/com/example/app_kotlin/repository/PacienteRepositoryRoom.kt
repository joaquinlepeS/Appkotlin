package com.example.app_kotlin.repository

import com.example.app_kotlin.data.AppDatabase
import com.example.app_kotlin.model.room.PacienteEntity

class PacienteRepositoryRoom(private val db: AppDatabase) {

    private val pacienteDao = db.pacientedao()

    suspend fun registrarPaciente(paciente: PacienteEntity) {
        pacienteDao.insertarPaciente(paciente)
    }

    suspend fun obtenerPacientePorEmail(email: String): PacienteEntity? {
        return pacienteDao.obtenerPacientePorEmail(email)
    }

    suspend fun obtenerTodos(): List<PacienteEntity> {
        return pacienteDao.obtenerPacientes()
    }

    suspend fun eliminarTodos() {
        pacienteDao.eliminarTodos()
    }
}
