package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.R
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.viewmodel.ConsultaApiViewModel
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.example.app_kotlin.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    usuarioViewModel: UsuarioViewModel,
    doctorViewModel: DoctorViewModel,
    consultaViewModel: ConsultaViewModel,
    consultaApiViewModel: ConsultaApiViewModel,
    onNavigateToConsultaCliente: () -> Unit
) {

    // Cargar datos al entrar
    LaunchedEffect(Unit) {
        doctorViewModel.fetchDoctors()
        consultaApiViewModel.cargarConsultasPorDoctor("")
    }

    // Estados UI
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    val doctores = doctorViewModel.doctors
    val consultasApi = consultaApiViewModel.consultas

    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    // Horas base
    val horarioBase = listOf(
        "09:00","09:30","10:00","10:30",
        "11:00","11:30","12:00",
        "14:00","14:30","15:00",
        "15:30","16:00","16:30"
    )

    // Ocupadas por ese doctor
    val consultasDelDoctor = consultasApi.filter { it.doctorEmail == doctorSeleccionado }
    val horasDisponibles = horarioBase.filter { h ->
        consultasDelDoctor.none { it.hora == h }
    }

    // ----------------------------------------
    //                UI
    // ----------------------------------------
    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo
        Image(
            painter = painterResource(R.drawable.wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(36.dp),
                modifier = Modifier.padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        "Agendar Consulta",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )

                    // ==========================
                    // ESPECIALIDAD
                    // ==========================
                    ExposedDropdownMenuBox(
                        expanded = expandedEspecialidad,
                        onExpandedChange = { expandedEspecialidad = !expandedEspecialidad }
                    ) {
                        OutlinedTextField(
                            value = especialidad,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Especialidad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEspecialidad) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .clickable { expandedEspecialidad = true }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedEspecialidad,
                            onDismissRequest = { expandedEspecialidad = false }
                        ) {
                            especialidades.forEach { esp ->
                                DropdownMenuItem(
                                    text = { Text(esp) },
                                    onClick = {
                                        especialidad = esp
                                        doctorSeleccionado = ""
                                        hora = ""
                                        expandedEspecialidad = false
                                    }
                                )
                            }
                        }
                    }

                    // ==========================
                    // DOCTOR
                    // ==========================
                    ExposedDropdownMenuBox(
                        expanded = expandedDoctor,
                        onExpandedChange = { expandedDoctor = !expandedDoctor }
                    ) {
                        OutlinedTextField(
                            value = doctorSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Doctor") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDoctor) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .clickable { expandedDoctor = true }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedDoctor,
                            onDismissRequest = { expandedDoctor = false }
                        ) {
                            doctoresFiltrados.forEach { doc ->
                                DropdownMenuItem(
                                    text = { Text("${doc.nombre}") },
                                    onClick = {
                                        doctorSeleccionado = doc.email
                                        hora = ""
                                        expandedDoctor = false
                                        consultaApiViewModel.cargarConsultasPorDoctor(doc.email)
                                    }
                                )
                            }
                        }
                    }

                    // ==========================
                    // HORAS DISPONIBLES
                    // ==========================
                    ExposedDropdownMenuBox(
                        expanded = expandedHora,
                        onExpandedChange = { expandedHora = !expandedHora }
                    ) {
                        OutlinedTextField(
                            value = hora,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Hora disponible") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedHora) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .clickable { expandedHora = true }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedHora,
                            onDismissRequest = { expandedHora = false }
                        ) {
                            horasDisponibles.forEach { h ->
                                DropdownMenuItem(
                                    text = { Text(h) },
                                    onClick = {
                                        hora = h
                                        expandedHora = false
                                    }
                                )
                            }
                        }
                    }

                    // ==========================
                    // CONFIRMAR
                    // ==========================
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = especialidad.isNotEmpty()
                                && doctorSeleccionado.isNotEmpty()
                                && hora.isNotEmpty(),
                        onClick = {
                            val user = usuarioViewModel.usuarioActual ?: return@Button

                            val nombreDoctor = doctores.firstOrNull { it.email == doctorSeleccionado }
                                ?.let { "${it.nombre}" }
                                ?: "Doctor"

                            consultaViewModel.agregarConsulta(
                                user.email,
                                Consulta(
                                    id = 0,
                                    fecha = "2025-11-24",
                                    hora = hora,
                                    especialidad = especialidad,
                                    doctor = nombreDoctor,
                                    paciente = user.nombre
                                )
                            )

                            onNavigateToConsultaCliente()
                        }
                    ) {
                        Text("Confirmar Consulta")
                    }
                }
            }
        }
    }
}
