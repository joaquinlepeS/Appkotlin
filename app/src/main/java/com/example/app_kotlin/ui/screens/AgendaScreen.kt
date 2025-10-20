package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // --- Variables de estado para los campos ---
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val doctores = appState.doctores // ✅ Se obtiene desde AppState

    var fechaError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agendar Consulta", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it
                            fechaError = validateFecha(fecha)},
            label = { Text("Fecha (ej: 2025-10-20)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (fechaError != null) {
            Text(
                text = fechaError!!,
                color = MaterialTheme.colorScheme.primaryContainer,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }


        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                doctores.forEach { doctor ->
                    DropdownMenuItem(
                        text = { Text(doctor.especialidad) },
                        onClick = {
                            doctorSeleccionado = doctor.especialidad
                            expanded = false
                        }
                    )
                }
            }
        }

        // --- Selector de Doctor ---
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                doctores.forEach { doctor ->
                    DropdownMenuItem(
                        text = { Text(doctor.nombre) },
                        onClick = {
                            doctorSeleccionado = doctor.nombre
                            expanded = false
                        }
                    )
                }
            }
        }

        // --- Botón de Confirmar ---
        Button(
            onClick = {
                if (fechaError==null&&
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

        // --- Mensaje si no hay doctores ---
        if (doctores.isEmpty()) {
            Text(
                text = "No hay doctores registrados aún.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
