package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.utils.validateFecha
import kotlinx.coroutines.launch

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

    // --- Cargar doctores desde DataStore ---
    LaunchedEffect(Unit) {
        appState.cargarDatos()
    }

    // --- Observar directamente la lista mutable de AppState ---
    val doctores = appState.doctores // mutableStateListOf<Doctor>, Compose detecta cambios

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agendar Consulta", style = MaterialTheme.typography.headlineSmall)

        // --- Campo de fecha ---
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

        // --- Campo de hora ---
        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Selector de especialidad ---
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
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedEspecialidad,
                onDismissRequest = { expandedEspecialidad = false }
            ) {
                doctores.map { it.especialidad }.distinct().forEach { esp ->
                    DropdownMenuItem(
                        text = { Text(esp) },
                        onClick = {
                            especialidad = esp
                            doctorSeleccionado = "" // limpiar doctor al cambiar especialidad
                            expandedEspecialidad = false
                        }
                    )
                }
            }
        }

        // --- Selector de doctor ---
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
                modifier = Modifier.fillMaxWidth(),
                enabled = especialidad.isNotBlank()
            )

            ExposedDropdownMenu(
                expanded = expandedDoctor,
                onDismissRequest = { expandedDoctor = false }
            ) {
                doctores.filter { it.especialidad == especialidad }.forEach { doctor ->
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

        // --- Botón de Confirmar ---
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
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
