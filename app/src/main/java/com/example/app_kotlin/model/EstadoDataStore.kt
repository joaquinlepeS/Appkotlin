package com.example.app_kotlin.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// DataStore "singleton"
val Context.dataStore by preferencesDataStore(name = "app_prefs")

class DataStoreManager(private val context: Context){
    private val gson = Gson()

    private val USERS_KEY = stringPreferencesKey("usuarios")
    private val CONSULTAS_KEY = stringPreferencesKey("consultas")

    private val DOCTORES_KEY = stringPreferencesKey("doctores")

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

    suspend fun saveNotes(notes: Map<String, List<String>>){
        val json = gson.toJson(notes)
        context.dataStore.edit { prefs ->
            prefs[CONSULTAS_KEY] = json
        }
    }

    fun getNotes(): Flow<Map<String, List<String>>>{
        return context.dataStore.data.map { prefs ->
            val json = prefs[CONSULTAS_KEY] ?: "{}"
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            gson.fromJson(json,type)
        }
    }

    suspend fun saveDoctores(users: List<Doctor>){
        val json = gson.toJson(users)
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = json
        }
    }

    fun getDoctores(): Flow<List<Doctor>>{
        return context.dataStore.data.map { prefs ->
            val json = prefs[DOCTORES_KEY] ?: "[]"
            val type = object : TypeToken<List<Usuario>>() {}.type
            gson.fromJson(json,type)
        }
    }

}