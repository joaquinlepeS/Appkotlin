package com.example.app_kotlin.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.app_kotlin.model.room.ConsultaEntity
import com.example.app_kotlin.room.dto.ConsultaConDoctorDTO


@Dao
interface ConsultaDao {

    @Insert
    suspend fun insertarConsulta(consulta: ConsultaEntity)

    @Query("SELECT * FROM consulta WHERE pacienteId = :pacienteId")
    suspend fun obtenerConsultasPorPaciente(pacienteId: Int): List<ConsultaEntity>

    @Query("DELETE FROM consulta WHERE id = :id")
    suspend fun eliminarConsulta(id: Int)

    // 🔥 JOIN profesional: obtener consulta + nombre del doctor
    @Query("""
        SELECT consulta.id, consulta.fecha, consulta.hora, consulta.especialidad,
               doctor.nombre AS doctorNombre, doctor.email AS doctorEmail
        FROM consulta
        INNER JOIN doctor ON consulta.doctorId = doctor.id
        WHERE consulta.pacienteId = :pacienteId
    """)
    suspend fun obtenerConsultasConDoctor(pacienteId: Int): List<ConsultaConDoctorDTO>
}
