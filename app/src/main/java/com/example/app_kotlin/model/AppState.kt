package com.example.app_kotlin.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Usuario(val email_user: String, val password_user: String, val nombre: String)
data class Doctor(val email_doc: String, val password_doc: String, val nombre: String, val especialidad: String)

data class Consulta(
    val id: Int,
    val fecha: String,
    val hora: String,
    val especialidad: String,
    val doctor: String,
    val paciente: String
)

class AppState(private val dataStore: DataStoreManager) {

    val usuarios = mutableStateListOf<Usuario>()
    val doctores = mutableStateListOf<Doctor>()

    val consultasPorUsuario = mutableMapOf<String, SnapshotStateList<Consulta>>()
    val consultasPorDoctor = mutableMapOf<String, SnapshotStateList<Consulta>>()

    var usuarioActual: Usuario? = null
    var doctorActual: Doctor? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    // Cargar datos persistidos
    suspend fun cargarDatos() {
        val users = dataStore.getUsers().first()
        val docs = dataStore.getDoctores().first()
        val consultas = dataStore.getConsulta().first()
        val consultasDoctor = dataStore.getConsultasPorDoctor().first()

        usuarios.clear()
        usuarios.addAll(users)

        doctores.clear()
        doctores.addAll(docs)

        consultasPorUsuario.clear()
        consultas.forEach { (key, value) ->
            consultasPorUsuario[key] = value.toMutableStateList()
        }

        consultasPorDoctor.clear()
        consultasDoctor.forEach { (key, value) ->
            consultasPorDoctor[key] = value.toMutableStateList()
        }

        // Logs para depuración
        println("✅ Datos cargados en memoria")
        println("Usuarios: $usuarios")
        println("Doctores: $doctores")
    }

    fun registrarUsuario(nombre: String, email: String, password: String): Boolean {
        if (usuarios.any { it.email_user == email }) return false
        val nuevo = Usuario(email, password, nombre)
        usuarios.add(nuevo)
        scope.launch { dataStore.saveUsers(usuarios) }
        return true
    }

    fun registrarDoctor(nombre: String, email: String, password: String, especialidad: String): Boolean {
        if (doctores.any { it.email_doc == email }) return false
        val nuevo = Doctor(email, password, nombre, especialidad)
        doctores.add(nuevo)
        scope.launch { dataStore.saveDoctores(doctores) }
        return true
    }

    fun loginUser(email: String, password: String): Boolean {
        val user = usuarios.find { it.email_user == email && it.password_user == password }
        return if (user != null) {
            usuarioActual = user
            true
        } else false
    }

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
        val emailUsuario = usuarioActual?.email_user ?: return
        val listaUsuario = consultasPorUsuario.getOrPut(emailUsuario) { mutableStateListOf() }
        val nextIdUsuario = if (listaUsuario.isEmpty()) 1 else (listaUsuario.maxOf { it.id } + 1)
        val nuevaConsulta = consulta.copy(id = nextIdUsuario, paciente = usuarioActual!!.nombre)
        listaUsuario.add(nuevaConsulta)
        scope.launch { dataStore.saveConsulta(consultasPorUsuario) }

        val doctorEmail = doctores.find { it.nombre == consulta.doctor }?.email_doc ?: return
        val listaDoctor = consultasPorDoctor.getOrPut(doctorEmail) { mutableStateListOf() }
        val nextIdDoctor = if (listaDoctor.isEmpty()) 1 else (listaDoctor.maxOf { it.id } + 1)
        listaDoctor.add(nuevaConsulta)
        scope.launch { dataStore.saveConsultasPorDoctor(consultasPorDoctor) }
    }

    fun obtenerConsultas(): List<Consulta> {
        val email = usuarioActual?.email_user ?: return emptyList()
        return consultasPorUsuario[email] ?: emptyList()
    }
}
