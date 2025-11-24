package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.viewmodel.UsuarioViewModel
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel

// Validaciones
import com.example.app_kotlin.utils.validateFecha
import com.example.app_kotlin.utils.validateHora
import com.example.app_kotlin.utils.validateEspecialidad
import com.example.app_kotlin.utils.validateDoctor
import com.example.app_kotlin.utils.validateAgenda


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    usuarioViewModel: UsuarioViewModel,
    consultaViewModel: ConsultaViewModel,
    onNavigateToConsultaCliente: () -> Unit
) {
    val context = LocalContext.current

    // Doctores API externa
    val doctorViewModel: DoctorViewModel = viewModel ()
    val doctores = doctorViewModel.doctors

    LaunchedEffect (Unit) {
        doctorViewModel.fetchDoctors()
    }

    // Campos del formulario
    var fecha by remember  { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }

    // Errores
    var fechaError by remember { mutableStateOf<String?>(null) }
    var horaError by remember { mutableStateOf<String?>(null) }
    var especialidadError by remember { mutableStateOf<String?>(null) }
    var doctorError by remember { mutableStateOf<String?>(null) }

    // Listas
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Agendar Consulta",
            style = MaterialTheme.typography.headlineSmall
        )

        // FECHA
        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                fechaError = validateFecha(fecha)
            },
            label = { Text("Fecha (ej: 2025-10-20)") },
            modifier = Modifier.fillMaxWidth()
        )
        if (fechaError != null) Text(fechaError!!, color = MaterialTheme.colorScheme.error)

        // HORA
        OutlinedTextField(
            value = hora,
            onValueChange = {
                hora = it
                horaError = validateHora(hora)
            },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )
        if (horaError != null) Text(horaError!!, color = MaterialTheme.colorScheme.error)

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
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecialidad) },
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
                            expandedEspecialidad = false
                        }
                    )
                }
            }
        }
        if (especialidadError != null) Text(especialidadError!!, color = MaterialTheme.colorScheme.error)

        // DOCTORES FILTRADOS
        ExposedDropdownMenuBox(
            expanded = expandedDoctor,
            onExpandedChange = { expandedDoctor = !expandedDoctor }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDoctor) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { if (especialidad.isNotBlank()) expandedDoctor = true }
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
                        }
                    )
                }
            }
        }
        if (doctorError != null) Text(doctorError!!, color = MaterialTheme.colorScheme.error)

        // BOTÓN CONFIRMAR
        Button(
            onClick = {
                val errors = validateAgenda(fecha, hora, especialidad, doctorSeleccionado)

                fechaError = errors.fechaError
                horaError = errors.horaError
                especialidadError = errors.especialidadError
                doctorError = errors.doctorError

                if (errors.todosNulos()) {

                    val consulta = Consulta(
                        id = 0,
                        fecha = fecha,
                        hora = hora,
                        especialidad = especialidad,
                        doctor = doctorSeleccionado,
                        paciente = usuarioViewModel.usuarioActual!!.nombre
                    )

                    consultaViewModel.agregarConsulta(
                        usuarioEmail = usuarioViewModel.usuarioActual!!.email,
                        consulta = consulta
                    )

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
