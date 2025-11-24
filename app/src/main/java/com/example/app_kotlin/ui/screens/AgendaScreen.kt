package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.example.app_kotlin.viewmodel.PacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    usuarioViewModel: PacienteViewModel,
    doctorViewModel: DoctorViewModel,
    consultaViewModel: ConsultaViewModel = viewModel(),
    onNavigateToConsultaCliente: () -> Unit
) {
    val doctorViewModel: DoctorViewModel = viewModel()

    // 🔵 Cargar API doctores + consultas globales
    LaunchedEffect(Unit) {
        doctorViewModel.fetchDoctors()
        val user = usuarioViewModel.usuarioActual
        if (user != null) {
            consultaViewModel.cargarConsultas(user.email)
        }

    }

    // Estados de formulario
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    // Listados
    val doctores = doctorViewModel.doctors
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Agendar Consulta", style = MaterialTheme.typography.headlineSmall)

        // -------------------------
        // ESPECIALIDAD
        // -------------------------
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

        // -------------------------
        // DOCTOR
        // -------------------------
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
                        text = { Text(doc.nombre) },
                        onClick = {
                            doctorSeleccionado = doc.email
                            hora = ""
                            expandedDoctor = false
                        }
                    )
                }
            }
        }

        // -------------------------
        // HORA DISPONIBLE
        // -------------------------
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

        }

        // -------------------------
        // CONFIRMAR CONSULTA
        // -------------------------
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val user = usuarioViewModel.usuarioActual ?: return@Button

                // Encontrar doctor por email
                val doctorNombre = doctoresFiltrados
                    .firstOrNull { it.email == doctorSeleccionado }
                    ?.nombre ?: "Doctor desconocido"

                consultaViewModel.agregarConsulta(
                    user.email,
                    Consulta(
                        id = 0,
                        fecha = "2025-11-24",   // luego pondremos DatePicker
                        hora = hora,
                        especialidad = especialidad,
                        doctor = doctorNombre,       // ✔ AHORA SE GUARDA EL NOMBRE
                        paciente = user.nombre       // ✔ AHORA SE GUARDA EL NOMBRE DEL PACIENTE
                    )
                )

                onNavigateToConsultaCliente()
            }
        ) {
            Text("Confirmar Consulta")
        }

    }
}
