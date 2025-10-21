package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.utils.validateFecha

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

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }

    var fechaError by remember { mutableStateOf<String?>(null) }

    val doctores = appState.doctores
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Agendar Consulta",
            style = MaterialTheme.typography.headlineSmall
        )

        // Campo Fecha
        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                fechaError = validateFecha(fecha)
            },
            label = { Text("Fecha (ej: 2025-10-20)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (fechaError != null) {
            Text(
                text = fechaError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        // Campo Hora
        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown Especialidad
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
                    .menuAnchor() // 👈 clave para que funcione bien
                    .fillMaxWidth()
                    .clickable { expandedEspecialidad = true } // 👈 abre con clic
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
                        }
                    )
                }
            }
        }

        // Dropdown Doctor
        ExposedDropdownMenuBox(
            expanded = expandedDoctor,
            onExpandedChange = { expandedDoctor = !expandedDoctor }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDoctor) },
                modifier = Modifier
                    .menuAnchor() // 👈 también aquí
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
                        }
                    )
                }
            }
        }

        // Botón Confirmar
        Button(
            onClick = {
                if (fechaError == null &&
                    fecha.isNotBlank() &&
                    hora.isNotBlank() &&
                    especialidad.isNotBlank() &&
                    doctorSeleccionado.isNotBlank()
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

        if (doctores.isEmpty()) {
            Text(
                text = "No hay doctores registrados aún.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
