package com.example.app_kotlin.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.app_kotlin.model.room.PacienteEntity

@Dao
interface PacienteDao {

    @Insert
    suspend fun insertarPaciente(paciente: PacienteEntity)

    @Query("SELECT * FROM paciente WHERE email = :email LIMIT 1")
    suspend fun obtenerPacientePorEmail(email: String): PacienteEntity?

    @Query("SELECT * FROM paciente WHERE id = :id LIMIT 1")
    suspend fun obtenerPacientePorId(id: Int): PacienteEntity?

    @Query("SELECT * FROM paciente")
    suspend fun obtenerPacientes(): List<PacienteEntity>

    @Query("DELETE FROM paciente")
    suspend fun eliminarTodos()

}
