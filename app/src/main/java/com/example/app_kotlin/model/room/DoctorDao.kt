package com.example.app_kotlin.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.app_kotlin.model.room.DoctorEntity

@Dao
interface DoctorDao {

    @Insert
    suspend fun insertarDoctor(doctor: DoctorEntity)

    @Insert
    suspend fun insertarDoctores(doctores: List<DoctorEntity>)

    @Query("SELECT * FROM doctor")
    suspend fun obtenerDoctores(): List<DoctorEntity>

    @Query("SELECT * FROM doctor WHERE id = :id LIMIT 1")
    suspend fun obtenerDoctorPorId(id: Int): DoctorEntity?

    @Query("DELETE FROM doctor")
    suspend fun eliminarTodos()
}
