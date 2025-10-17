package com.example.app_kotlin.model

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first



// DataStore "singleton"
val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context){
    private val gson = Gson()

    private val USERS_KEY = stringPreferencesKey("usuarios")
    private val CONSULTAS_KEY = stringPreferencesKey("consultas")
    private val DOCTORES_KEY = stringPreferencesKey("doctores")
    private val CONSULTAS_DOCTOR_KEY = stringPreferencesKey("consultas_por_doctor")

    suspend fun saveUsers(users: List<Usuario>){
        val json = gson.toJson(users)
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = json
        }
    }

    fun getUsers(): Flow<List<Usuario>>{
        return context.dataStore.data.map { prefs ->
            val json = prefs[USERS_KEY] ?: "[]"
            val type = object : TypeToken<List<Usuario>>() {}.type
            gson.fromJson(json,type)
        }
    }

    suspend fun saveConsulta(consultas: Map<String, List<Consulta>>) {
        val json = gson.toJson(consultas)
        context.dataStore.edit { prefs ->
            prefs[CONSULTAS_KEY] = json
        }
    }

    fun getConsulta(): Flow<Map<String, List<Consulta>>> = context.dataStore.data.map { prefs ->
        val json = prefs[CONSULTAS_KEY] ?: "{}"
        val type = object : TypeToken<Map<String, List<Consulta>>>() {}.type
        gson.fromJson<Map<String, List<Consulta>>>(json, type) ?: emptyMap()
    }

    suspend fun saveDoctores(users: List<Doctor>){
        val json = gson.toJson(users)
        context.dataStore.edit { prefs ->
            prefs[DOCTORES_KEY] = json
        }
    }

    fun getDoctores(): Flow<List<Doctor>>{
        return context.dataStore.data.map { prefs ->
            val json = prefs[DOCTORES_KEY] ?: "[]"
            val type = object : TypeToken<List<Doctor>>() {}.type
            gson.fromJson(json,type)
        }
    }

    suspend fun saveConsultasPorDoctor(consultasPorDoctor: Map<String, List<Consulta>>) {
        val json = gson.toJson(consultasPorDoctor)
        context.dataStore.edit { prefs ->
            prefs[CONSULTAS_DOCTOR_KEY] = json
        }
    }

    fun getConsultasPorDoctor(): Flow<Map<String, List<Consulta>>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[CONSULTAS_DOCTOR_KEY] ?: "{}"
            val type = object : TypeToken<Map<String, List<Consulta>>>() {}.type
            gson.fromJson<Map<String, List<Consulta>>>(json, type) ?: emptyMap()
        }
    }

    suspend fun agregarConsultaParaDoctor(doctorId: String, nuevaConsulta: Consulta) {
        // Obtener las consultas actuales por doctor
        val consultasActuales = getConsultasPorDoctor().first().toMutableMap()

        // Obtener la lista actual del doctor o crear una nueva
        val listaConsultas = consultasActuales[doctorId]?.toMutableList() ?: mutableListOf()

        // Agregar la nueva consulta
        listaConsultas.add(nuevaConsulta)

        // Guardar los cambios actualizados
        consultasActuales[doctorId] = listaConsultas
        saveConsultasPorDoctor(consultasActuales)
    }





}