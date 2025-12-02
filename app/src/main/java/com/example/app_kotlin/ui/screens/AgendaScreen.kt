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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    `pacienteViewModel.kt`: `PacienteViewModel.kt`,
    doctorViewModel: DoctorViewModel,
    consultaViewModel: ConsultaViewModel = viewModel(),
    consultaApiViewModel: ConsultaApiViewModel = viewModel(),
    onNavigateToConsultaCliente: () -> Unit
) {

    val context = LocalContext.current

    // Cargar datos del doctor y consultas del usuario
    LaunchedEffect(Unit) {
        doctorViewModel.fetchDoctors()
        `pacienteViewModel.kt`.pacienteActual?.let {
            consultaViewModel.cargarConsultas(it.email)
        }
    }

    // Estados UI
    var especialidad by remember { mutableStateOf("") }
    var doctorNombreMostrado by remember { mutableStateOf("") }
    var doctorEmailSeleccionado by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    val doctores = doctorViewModel.doctors
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    val consultasApi = consultaApiViewModel.consultas

    val horarioBase = listOf(
        "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00",
        "14:00", "14:30", "15:00",
        "15:30", "16:00", "16:30"
    )

    val horasOcupadas =
        consultasApi.filter { it.doctorEmail == doctorEmailSeleccionado }.map { it.hora }

    val horasDisponibles = horarioBase.filter { it !in horasOcupadas }

    // ---------------- UI ----------------

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Agendar Consulta",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            content = { innerPadding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {

                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(36.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .padding(16.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(20.dp)
                        ) {

                            // ESPECIALIDAD
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
                                                doctorNombreMostrado = ""
                                                doctorEmailSeleccionado = ""
                                                hora = ""
                                                expandedEspecialidad = false
                                            }
                                        )
                                    }
                                }
                            }

                            // DOCTOR
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
                                        .clickable { expandedDoctor = true }
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
                                                doctorEmailSeleccionado = doc.email
                                                hora = ""
                                                expandedDoctor = false
                                            }
                                        )
                                    }
                                }
                            }

                            // HORA
                            ExposedDropdownMenuBox(
                                expanded = expandedHora,
                                onExpandedChange = { expandedHora = !expandedHora }
                            ) {
                                OutlinedTextField(
                                    value = hora,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Hora Disponible") },
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

                            // CONFIRMAR
                            Button(
                                onClick = {
                                    val user = `pacienteViewModel.kt`.pacienteActual ?: return@Button

                                    consultaViewModel.agregarConsulta(
                                        user.email,
                                        Consulta(
                                            id = 0,
                                            fecha = "2025-11-24",
                                            hora = hora,
                                            especialidad = especialidad,
                                            doctor = doctorNombreMostrado,
                                            paciente = user.nombre
                                        )
                                    )

                                    showAgendaNotification(
                                        context = context,
                                        message = "Consulta agendada con $doctorNombreMostrado a las $hora"
                                    )

                                    onNavigateToConsultaCliente()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Confirmar Consulta")
                            }
                        }
                    }
                }
            }
        )
    }
}
