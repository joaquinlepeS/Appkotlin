package com.example.app_kotlin.repository

import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.remote.ConsultaRetrofit

class ConsultaRepository {

    suspend fun getAll(): List<Consulta> {
        println("DEBUG → Repository.getAll() llamado")
        return ConsultaRetrofit.api.getConsultas()
    }

    suspend fun getById(id: Long): Consulta {
        println("DEBUG → Repository.getById($id) llamado")
        return ConsultaRetrofit.api.getConsultaById(id)
    }

    suspend fun getByDoctor(doctorId: Long): List<Consulta> {
        println("DEBUG → Repository.getByDoctor($doctorId) llamado")
        return ConsultaRetrofit.api.getConsultasPorDoctor(doctorId)
    }

    suspend fun getByPaciente(pacienteId: Long): List<Consulta> {
        println("DEBUG → Repository.getByPaciente($pacienteId) llamado")
        return ConsultaRetrofit.api.getConsultasPorPaciente(pacienteId)
    }

    suspend fun update(id: Long, consulta: Consulta): Consulta {
        println("DEBUG → Repository.update($id) llamado")
        return ConsultaRetrofit.api.updateConsulta(id, consulta)
    }

    suspend fun delete(id: Long) {
        println("DEBUG → Repository.delete($id) llamado")
        ConsultaRetrofit.api.deleteConsulta(id)
    }
}
