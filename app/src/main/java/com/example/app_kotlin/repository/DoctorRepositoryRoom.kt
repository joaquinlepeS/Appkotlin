package com.example.app_kotlin.repository

import android.content.Context
import com.example.app_kotlin.data.DatabaseClient
import com.example.app_kotlin.model.room.DoctorEntity

class DoctorRepositoryRoom(context: Context) {

    private val doctorDao = DatabaseClient.getDatabase(context).doctorDao()

    suspend fun insertarDoctor(doctor: DoctorEntity) {
        doctorDao.insertarDoctor(doctor)
    }

    suspend fun insertarDoctores(lista: List<DoctorEntity>) {
        doctorDao.insertarDoctores(lista)
    }

    suspend fun obtenerDoctores(): List<DoctorEntity> {
        return doctorDao.obtenerDoctores()
    }

    suspend fun obtenerDoctorPorId(id: Int): DoctorEntity? {
        return doctorDao.obtenerDoctorPorId(id)
    }

    suspend fun eliminarTodos() {
        doctorDao.eliminarTodos()
    }
}
