package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.R
import com.example.app_kotlin.viewmodel.PacienteViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.example.app_kotlin.viewmodel.ConsultaViewModel

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

    LaunchedEffect(Unit) {
        doctorViewModel.fetchDoctors()
        consultaViewModel.loadAll()
    }

    var especialidad by remember { mutableStateOf("") }
    var doctorNombreMostrado by remember { mutableStateOf("") }
    var doctorIdSeleccionado by remember { mutableStateOf<Long?>(null) }
    var horaSeleccionada by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    val especialidades = doctores.map { it.especialidad }.distinct()

    val doctoresFiltrados by remember(especialidad, doctores) {
        mutableStateOf(if (especialidad.isNotEmpty()) doctores.filter { it.especialidad == especialidad } else emptyList())
    }

    val consultasDelDoctor by remember(doctorIdSeleccionado, consultas) {
        mutableStateOf(
            if (doctorIdSeleccionado != null)
                consultas.filter { it.doctor?.id == doctorIdSeleccionado }
            else emptyList()
        )
    }

    val consultasLibres by remember(consultasDelDoctor) {
        mutableStateOf(consultasDelDoctor.filter { it.paciente == null })
    }

    val horasDisponibles by remember(consultasLibres) {
        mutableStateOf(consultasLibres.map { "${it.fecha} - ${it.hora}" })
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agendar Consulta",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    elevation = CardDefaults.cardElevation(20.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        // ---- UI ORIGINAL SIN CAMBIAR LÓGICA ----

                        ExposedDropdownMenuBox(
                            expanded = expandedEspecialidad,
                            onExpandedChange = { expandedEspecialidad = !expandedEspecialidad }
                        ) {
                            OutlinedTextField(
                                value = especialidad,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Especialidad") },
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

                        ExposedDropdownMenuBox(
                            expanded = expandedDoctor,
                            onExpandedChange = { expandedDoctor = !expandedDoctor }
                        ) {
                            OutlinedTextField(
                                value = doctorNombreMostrado,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Doctor") },
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

                        ExposedDropdownMenuBox(
                            expanded = expandedHora,
                            onExpandedChange = { expandedHora = !expandedHora }
                        ) {
                            OutlinedTextField(
                                value = horaSeleccionada,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Hora Disponible") },
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

                        Button(
                            onClick = {
                                if (paciente == null) return@Button
                                val consultaLibre = consultasLibres.firstOrNull {
                                    "${it.fecha} - ${it.hora}" == horaSeleccionada
                                }

                                if (consultaLibre != null) {
                                    val consultaActualizada = consultaLibre.copy(paciente = paciente)
                                    consultaViewModel.updateConsulta(consultaLibre.id!!, consultaActualizada)
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
        }
    }
}
