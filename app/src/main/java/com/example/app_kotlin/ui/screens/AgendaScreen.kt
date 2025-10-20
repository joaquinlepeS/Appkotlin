package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.utils.*
import com.example.app_kotlin.model.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    onNavigateToConsultaCliente: () -> Unit,
    appState: AppState
) {
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }

    var fechaError by remember { mutableStateOf<String?>(null) }
    var horaError by remember { mutableStateOf<String?>(null) }
    var especialidadError by remember { mutableStateOf<String?>(null) }
    var doctorError by remember { mutableStateOf<String?>(null) }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }

    val doctores = appState.doctores
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agendar Consulta", style = MaterialTheme.typography.headlineSmall)

        // Fecha
        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                fechaError = validateFecha(fecha)
            },
            label = { Text("Fecha (ej: 2025-10-20)") },
            modifier = Modifier.fillMaxWidth(),
            isError = fechaError != null
        )
        if (fechaError != null) {
            Text(fechaError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // Hora
        OutlinedTextField(
            value = hora,
            onValueChange = {
                hora = it
                horaError = validateHora(hora)
            },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth(),
            isError = horaError != null
        )
        if (horaError != null) {
            Text(horaError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // Especialidad
        ExposedDropdownMenuBox(
            expanded = expandedEspecialidad,
            onExpandedChange = { expandedEspecialidad = !expandedEspecialidad }
        ) {
            OutlinedTextField(
                value = especialidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar especialidad") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecialidad) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expandedEspecialidad = true },
                isError = especialidadError != null
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
                            expandedEspecialidad = false
                            doctorSeleccionado = ""
                            especialidadError = null
                        }
                    )
                }
            }
        }
        if (especialidadError != null) {
            Text(especialidadError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // Doctor
        ExposedDropdownMenuBox(
            expanded = expandedDoctor,
            onExpandedChange = { expandedDoctor = !expandedDoctor }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDoctor) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { if (especialidad.isNotBlank()) expandedDoctor = true },
                enabled = especialidad.isNotBlank(),
                isError = doctorError != null
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
                            doctorError = null
                        }
                    )
                }
            }
        }
        if (doctorError != null) {
            Text(doctorError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        // Botón Confirmar
        Button(
            onClick = {
                val errors = validateAgenda(fecha, hora, especialidad, doctorSeleccionado)
                fechaError = errors.fechaError
                horaError = errors.horaError
                especialidadError = errors.especialidadError
                doctorError = errors.doctorError

                if (errors.fechaError == null &&
                    errors.horaError == null &&
                    errors.especialidadError == null &&
                    errors.doctorError == null
                ) {
                    val nuevaConsulta = Consulta(
                        id = 0,
                        fecha = fecha,
                        hora = hora,
                        especialidad = especialidad,
                        doctor = doctorSeleccionado,
                        paciente = appState.usuarioActual?.nombre ?: "Desconocido"
                    )
                    appState.agregarConsulta(nuevaConsulta)
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
