package com.myapplication.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Usuario(val email_user: String, val password_user: String)
data class Doctor(val nombre: String,val rut:String,val email_doc: String,val password_doc: String)

class AppState(private  val dataStore: DataStoreManager){
    val usuarios = mutableStateListOf<Usuario>()
    val doctores = mutableStateListOf<Doctor>()
    var usuarioActual: Usuario? = null
    var doctorActual: Doctor? = null
    val consultasPorUsuario = mutableStateMapOf<String, SnapshotStateList<String>>()

    private val scope = CoroutineScope(Dispatchers.IO)

    //Carga de datos iniciales
    fun cargarDatos(){
        scope.launch {
            val users = dataStore.getUsers().first()
            val consultas = dataStore.getNotes().first()
            val docs= dataStore.getDoctores().first()

            usuarios.clear()
            usuarios.addAll(users)

            doctores.clear()
            doctores.addAll(docs)

            consultasPorUsuario.clear()
            consultas.forEach { (k,v) ->
                consultasPorUsuario[k] = v.toMutableStateList()
            }
        }
    }

    //Registro de usuario
    fun registrarUsuario(email: String, password: String): Boolean{
        if (usuarios.any{ it.email_user == email }) return false
        val nuevo = Usuario(email, password)
        usuarios.add(nuevo)
        guardarUsuarios()
        return true
    }

    fun registrarDoctor( nombre: String, rut:String, email_doc: String, password_doc: String):Boolean {
        if(doctores.any{ it.email_doc == email_doc}) return false
        val nuevo = Doctor(nombre = nombre,rut=rut,email_doc=email_doc,password_doc=password_doc)
        doctores.add(nuevo)
        guardarDoctores()
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
        val doc = doctores.find { it.nombre == nombre && it.rut == rut && it.email_doc == email_doc && it.password_doc == password_doc }
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

    private fun guardarDoctores(){
        scope.launch {
            dataStore.saveDoctores(doctores)
        }
    }
}
