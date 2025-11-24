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
import com.example.app_kotlin.viewmodel.ConsultaApiViewModel
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.example.app_kotlin.viewmodel.UsuarioViewModel
import com.example.app_kotlin.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    usuarioViewModel: UsuarioViewModel,
    consultaViewModel: ConsultaViewModel,
    onNavigateToConsultaCliente: () -> Unit
) {
    // ViewModels externos
    val doctorViewModel: DoctorViewModel = viewModel()
    val consultaApiViewModel: ConsultaApiViewModel = viewModel()

    // Cargar doctores al entrar
    LaunchedEffect(Unit) {
        doctorViewModel.fetchDoctors()
    }

    // STATES
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    // ERRORES
    var especialidadError by remember { mutableStateOf<String?>(null) }
    var doctorError by remember { mutableStateOf<String?>(null) }
    var horaError by remember { mutableStateOf<String?>(null) }

    // LISTAS de doctores
    val doctores = doctorViewModel.doctors
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    // HORAS DISPONIBLES desde API
    val horasDisponibles = consultaApiViewModel.consultas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Agendar Consulta", style = MaterialTheme.typography.headlineSmall)

        // 🔵 ESPECIALIDAD
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
                            fecha = ""
                            hora = ""
                            expandedEspecialidad = false
                            especialidadError = validateEspecialidad(especialidad)
                        }
                    )
                }
            }
        }
        if (especialidadError != null) Text(especialidadError!!, color = MaterialTheme.colorScheme.error)

        // 🔵 DOCTOR
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
                    .clickable {
                        if (especialidad.isNotBlank()) expandedDoctor = true
                    },
                enabled = especialidad.isNotBlank()
            )
            ExposedDropdownMenu(
                expanded = expandedDoctor,
                onDismissRequest = { expandedDoctor = false }
            ) {
                doctoresFiltrados.forEach { doctor ->
                    DropdownMenuItem(
                        text = { Text(doctor.nombre) },
                        onClick = {
                            doctorSeleccionado = doctor.nombre
                            expandedDoctor = false
                            doctorError = validateDoctor(doctorSeleccionado)

                            // CARGAR HORAS DESDE API
                            consultaApiViewModel.cargarConsultasPorDoctor(doctor.email)
                        }
                    )
                }
            }
        }
        if (doctorError != null) Text(doctorError!!, color = MaterialTheme.colorScheme.error)

        // 🔵 HORAS DISPONIBLES DESDE API
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
                    .clickable {
                        if (consultaApiViewModel.consultas.isNotEmpty())
                            expandedHora = true
                    },
                enabled = doctorSeleccionado.isNotBlank()
            )
            ExposedDropdownMenu(
                expanded = expandedHora,
                onDismissRequest = { expandedHora = false }
            ) {
                horasDisponibles.forEach { consulta ->
                    DropdownMenuItem(
                        text = { Text("${consulta.fecha} - ${consulta.hora}") },
                        onClick = {
                            fecha = consulta.fecha
                            hora = consulta.hora
                            expandedHora = false
                            horaError = validateHora(hora)
                        }
                    )
                }
            }
        }
        if (horaError != null) Text(horaError!!, color = MaterialTheme.colorScheme.error)

        // 🔵 BOTÓN CONFIRMAR
        Button(
            onClick = {
                // Validaciones
                val errors = validateAgenda(fecha, hora, especialidad, doctorSeleccionado)
                especialidadError = errors.especialidadError
                doctorError = errors.doctorError
                horaError = errors.horaError

                if (errors.todosNulos()) {
                    val consulta = Consulta(
                        id = 0,
                        fecha = fecha,
                        hora = hora,
                        especialidad = especialidad,
                        doctor = doctorSeleccionado,
                        paciente = usuarioViewModel.usuarioActual!!.nombre
                    )

                    consultaViewModel.agregarConsulta(usuarioViewModel.usuarioActual!!.email, consulta)

                    onNavigateToConsultaCliente()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = doctores.isNotEmpty()
        ) {
            Text("Confirmar")
        }
    }
}
