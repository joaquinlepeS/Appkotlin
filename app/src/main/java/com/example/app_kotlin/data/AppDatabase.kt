package com.example.app_kotlin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app_kotlin.model.room.DoctorEntity
import com.example.app_kotlin.model.room.ConsultaEntity
import com.example.app_kotlin.model.room.PacienteEntity
import com.example.app_kotlin.model.room.DoctorDao
import com.example.app_kotlin.model.room.ConsultaDao
import com.example.app_kotlin.model.room.PacienteDao

@Database(
    entities = [
        DoctorEntity::class,
        ConsultaEntity::class,
        PacienteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun doctorDao(): DoctorDao
    abstract fun consultaDao(): ConsultaDao
    abstract fun pacientedao(): PacienteDao
}
