package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.Consulta

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

    val doctores = appState.doctores
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agendar Consulta", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (ej: 2025-10-20)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = especialidad,
            onValueChange = { especialidad = it },
            label = { Text("Especialidad") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Selector de doctor ---
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
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

        // --- Botón Confirmar ---
        Button(
            onClick = {
                if (fecha.isNotBlank() &&
                    hora.isNotBlank() &&
                    especialidad.isNotBlank() &&
                    doctorSeleccionado.isNotBlank()
                ) {
                    // 👇 Aquí va tu código para crear y guardar la consulta
                    val nuevaConsulta = Consulta(
                        id = 0, // se reemplaza en agregarConsulta()
                        fecha = fecha,
                        hora = hora,
                        especialidad = especialidad,
                        doctor = doctorSeleccionado
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
                "No hay doctores registrados aún.",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
