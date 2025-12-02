package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.R
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.utils.showAgendaNotification
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.example.app_kotlin.viewmodel.PacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    pacienteViewModel: PacienteViewModel,
    doctorViewModel: DoctorViewModel,
    consultaViewModel: ConsultaViewModel,
    onNavigateToConsultaCliente: () -> Unit
) {
    val paciente = pacienteViewModel.pacienteActual
    val doctores = doctorViewModel.doctors
    val consultas = consultaViewModel.consultas

    // ---------------- LOAD DATA ----------------
    LaunchedEffect(Unit) {
        doctorViewModel.fetchDoctors()       // doctores reales del backend
        consultaViewModel.loadAll()     // consultas reales del backend
    }

    // ---------------- Estados UI ----------------
    var especialidad by remember { mutableStateOf("") }
    var doctorNombreMostrado by remember { mutableStateOf("") }
    var doctorIdSeleccionado by remember { mutableStateOf<Long?>(null) }
    var horaSeleccionada by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    val especialidades = doctores.map { it.especialidad }.distinct()

    val doctoresFiltrados =
        if (especialidad.isNotEmpty())
            doctores.filter { it.especialidad == especialidad }
        else emptyList()

    // Consultas del doctor seleccionado
    val consultasDelDoctor =
        if (doctorIdSeleccionado != null)
            consultas.filter { it.doctor?.id == doctorIdSeleccionado }
        else emptyList()

    // Solo consultas libres (paciente_id = null)
    val consultasLibres = consultasDelDoctor.filter { it.paciente == null }

    // Horas disponibles reales desde la BD
    val horasDisponibles = consultasLibres.map { it.hora }

    // ---------------- UI ----------------

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agendar Consulta") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------- ESPECIALIDAD ----------
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
                                doctorNombreMostrado = ""
                                doctorIdSeleccionado = null
                                horaSeleccionada = ""
                                expandedEspecialidad = false
                            }
                        )
                    }
                }
            }

            // ---------- DOCTOR ----------
            ExposedDropdownMenuBox(
                expanded = expandedDoctor,
                onExpandedChange = { expandedDoctor = !expandedDoctor }
            ) {
                OutlinedTextField(
                    value = doctorNombreMostrado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Doctor") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDoctor) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedDoctor,
                    onDismissRequest = { expandedDoctor = false }
                ) {
                    doctoresFiltrados.forEach { doc ->
                        DropdownMenuItem(
                            text = { Text(doc.nombre) },
                            onClick = {
                                doctorNombreMostrado = doc.nombre
                                doctorIdSeleccionado = doc.id
                                horaSeleccionada = ""
                                expandedDoctor = false
                            }
                        )
                    }
                }
            }

            // ---------- HORAS ----------
            ExposedDropdownMenuBox(
                expanded = expandedHora,
                onExpandedChange = { expandedHora = !expandedHora }
            ) {
                OutlinedTextField(
                    value = horaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hora Disponible") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedHora) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedHora,
                    onDismissRequest = { expandedHora = false }
                ) {
                    horasDisponibles.forEach { h ->
                        DropdownMenuItem(
                            text = { Text(h) },
                            onClick = {
                                horaSeleccionada = h
                                expandedHora = false
                            }
                        )
                    }
                }
            }

            // ---------- CONFIRMAR ----------
            Button(
                onClick = {
                    if (paciente == null) return@Button

                    // Buscar consulta libre por hora
                    val consultaLibre = consultasLibres.firstOrNull {
                        it.hora == horaSeleccionada
                    }

                    if (consultaLibre != null) {
                        val consultaActualizada = consultaLibre.copy(
                            paciente = paciente
                        )

                        consultaViewModel.updateConsulta(
                            id = consultaLibre.id!!,
                            consulta = consultaActualizada
                        )

                        onNavigateToConsultaCliente()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Consulta")
            }
        }
    }
}
