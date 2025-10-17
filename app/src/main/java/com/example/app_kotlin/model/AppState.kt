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


class AppState(private  val dataStore: DataStoreManager){
    val usuarios = mutableStateListOf<Usuario>()
    val doctores = mutableStateListOf<Doctor>()
    var usuarioActual: Usuario? = null
    var doctorActual: Doctor? = null
    val consultasPorUsuario = mutableStateMapOf<String, SnapshotStateList<String>>()

    private val scope = CoroutineScope(Dispatchers.IO)

    //Carga de datos iniciales
    suspend fun cargarDatos() {
        val users = dataStore.getUsers().first()
        val consultas = dataStore.getNotes().first()
        val docs = dataStore.getDoctores().first()

        usuarios.clear()
        usuarios.addAll(users)

        doctores.clear()
        doctores.addAll(docs)

        consultasPorUsuario.clear()
        consultas.forEach { (k, v) ->
            consultasPorUsuario[k] = v.toMutableStateList()
        }
    }


    //Registro de usuario
    fun registrarUsuario(email: String, password: String,usuario: String): Boolean{
        if (usuarios.any{ it.email_user == email }) return false
        val nuevo = Usuario(email, password,usuario)
        usuarios.add(nuevo)
        guardarUsuarios()
        return true
    }

    fun registrarDoctor( email_doc: String, password_doc: String,usuario:String):Boolean {
        if(doctores.any{ it.email_doc == email_doc}) return false
        val nuevo = Doctor(email_doc=email_doc,password_doc=password_doc,usuario)
        doctores.add(nuevo)
        guardarDoctores()
        return true
    }

    //G
    fun agregarConsultas(consulta: String){
        val email = usuarioActual?.email_user ?: return
        val consultas = consultasPorUsuario.getOrPut(email){ mutableStateListOf()}
        consultas.add(consulta)
        guardarNotas()
    }

    //login
    fun loginUser(email: String,password: String) : Boolean{
        val user = usuarios.find { it.email_user == email && it.password_user == password }
        return if(user != null){
            usuarioActual = user
            true
        }else false
    }

    fun loginDoctor(nombre: String, rut:String, email_doc: String, password_doc: String):Boolean{
        val doc = doctores.find { it.email_doc == email_doc && it.password_doc == password_doc }
        return if(doc != null){
            doctorActual = doc
            true
        }else false
    }

    //logout
    fun logout(){
        usuarioActual = null
        doctorActual = null
    }

    //obtener notas del usuario logeado
    fun obtenerConsultas(): List<String>{
        val email = usuarioActual?.email_user ?: return emptyList()
        return consultasPorUsuario[email] ?: mutableStateListOf()
    }



    //Guardar en DataStore
    private fun guardarUsuarios(){
        scope.launch {
            dataStore.saveUsers(usuarios)
        }
    }

    private fun guardarNotas(){
        scope.launch {
            dataStore.saveNotes(consultasPorUsuario)
        }
    }

    private  fun guardarDoctores(){
        scope.launch {
            dataStore.saveDoctores(doctores)
        }
    }
}
