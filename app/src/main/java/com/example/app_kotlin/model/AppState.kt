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

data class Consulta(val id: Int,val fecha: String,val hora: String,val especialidad: String,val doctor: String)



class AppState(private val dataStore: DataStoreManager) {

    val usuarios = mutableStateListOf<Usuario>()
    val doctores = mutableStateListOf<Doctor>()

    val consultasPorUsuario = mutableStateMapOf<String, SnapshotStateList<Consulta>>()

    var usuarioActual: Usuario? = null
    var doctorActual: Doctor? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    // cargar datos persistidos
    suspend fun cargarDatos() {
        val users = dataStore.getUsers().first()
        val docs = dataStore.getDoctores().first()
        val consultas = dataStore.getConsulta().first()

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

    fun agregarConsulta(consulta: Consulta) {
        val email = usuarioActual?.email_user ?: return

        val lista = consultasPorUsuario.getOrPut(email) { mutableStateListOf() }

        // Calcular el siguiente ID para este usuario
        val nextId = if (lista.isEmpty()) 1 else (lista.maxOf { it.id } + 1)

        // Crear nueva consulta con ID autoincrementado
        val nuevaConsulta = consulta.copy(id = nextId)

        // Agregarla a la lista
        lista.add(nuevaConsulta)

        // Guardar persistencia
        scope.launch { dataStore.saveConsulta(consultasPorUsuario) }
    }

    fun obtenerConsultas(): List<Consulta> {
        val email = usuarioActual?.email_user ?: return emptyList()
        return consultasPorUsuario[email] ?: emptyList()
    }
}
