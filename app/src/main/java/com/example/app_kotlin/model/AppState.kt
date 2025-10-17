package com.example.app_kotlin.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.app_kotlin.model.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Usuario(val email_user: String, val password_user: String, val nombre: String)
data class Doctor(val email_doc: String, val password_doc: String, val nombre: String)





class AppState(private val dataStore: DataStoreManager) {

    val usuarios = mutableStateListOf<Usuario>()
    val doctores = mutableStateListOf<Doctor>()
    val consultasPorUsuario = mutableStateMapOf<String, SnapshotStateList<String>>()

    var usuarioActual: Usuario? = null
    var doctorActual: Doctor? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    // cargar datos persistidos
    suspend fun cargarDatos() {
        val users = dataStore.getUsers().first()
        val docs = dataStore.getDoctores().first()
        val consultas = dataStore.getNotes().first()

        usuarios.clear()
        usuarios.addAll(users)

        doctores.clear()
        doctores.addAll(docs)

        consultasPorUsuario.clear()
        consultas.forEach { (key, value) ->
            consultasPorUsuario[key] = value.toMutableStateList()
        }
    }

    // registrar usuario
    fun registrarUsuario(nombre: String, email: String, password: String): Boolean {
        if (usuarios.any { it.email_user == email }) return false
        val nuevo = Usuario(nombre, email, password)
        usuarios.add(nuevo)
        scope.launch { dataStore.saveUsers(usuarios) }
        return true
    }

    // registrar doctor
    fun registrarDoctor(nombre: String, email: String, password: String): Boolean {
        if (doctores.any { it.email_doc == email }) return false
        val nuevo = Doctor(nombre, email, password)
        doctores.add(nuevo)
        scope.launch { dataStore.saveDoctores(doctores) }
        return true
    }

    // login usuario
    fun loginUser(email: String, password: String): Boolean {
        val user = usuarios.find { it.email_user == email && it.password_user == password }
        return if (user != null) {
            usuarioActual = user
            true
        } else false
    }

    // login doctor
    fun loginDoctor(email: String, password: String): Boolean {
        val doc = doctores.find { it.email_doc == email && it.password_doc == password }
        return if (doc != null) {
            doctorActual = doc
            true
        } else false
    }

    fun logout() {
        usuarioActual = null
        doctorActual = null
    }

    fun agregarConsultas(consulta: String) {
        val email = usuarioActual?.email_user ?: return
        val consultas = consultasPorUsuario.getOrPut(email) { mutableStateListOf() }
        consultas.add(consulta)
        scope.launch { dataStore.saveNotes(consultasPorUsuario) }
    }

    fun obtenerConsultas(): List<String> {
        val email = usuarioActual?.email_user ?: return emptyList()
        return consultasPorUsuario[email] ?: mutableStateListOf()
    }
}
