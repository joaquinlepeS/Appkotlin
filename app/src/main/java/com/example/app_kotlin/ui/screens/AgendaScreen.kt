package com.example.app_kotlin.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    onNavigateToConsultaCliente: () -> Unit,
    appState: AppState
) {
    val context = LocalContext.current

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var doctorSeleccionado by remember { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }

    var fechaError by remember { mutableStateOf<String?>(null) }
    var horaError by remember { mutableStateOf<String?>(null) }
    var especialidadError by remember { mutableStateOf<String?>(null) }
    var doctorError by remember { mutableStateOf<String?>(null) }

    val doctores = appState.doctores
    val especialidades = doctores.map { it.especialidad }.distinct()
    val doctoresFiltrados = doctores.filter { it.especialidad == especialidad }

    // --- Permiso de notificación ---
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        // No hacemos nada extra aquí, solo pedimos permiso
    }

    LaunchedEffect(Unit) {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // --- Aquí sigue exactamente todo tu código existente ---
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

        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                fechaError = validateFecha(fecha)
            },
            label = { Text("Fecha (ej: 2025-10-20)") },
            modifier = Modifier.fillMaxWidth()
        )
        if (fechaError != null) Text(fechaError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

        OutlinedTextField(
            value = hora,
            onValueChange = {
                hora = it
                horaError = validateHora(hora)
            },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )
        if (horaError != null) Text(horaError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

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
                            expandedEspecialidad = false
                            doctorSeleccionado = ""
                            especialidadError = validateEspecialidad(especialidad)
                        }
                    )
                }
            }
        }
        if (especialidadError != null) Text(especialidadError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

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
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { if (especialidad.isNotBlank()) expandedDoctor = true },
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
                        }
                    )
                }
            }
        }
        if (doctorError != null) Text(doctorError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)

        Button(
            onClick = {
                val errors = validateAgenda(fecha, hora, especialidad, doctorSeleccionado)
                fechaError = errors.fechaError
                horaError = errors.horaError
                especialidadError = errors.especialidadError
                doctorError = errors.doctorError

                if (fechaError == null && horaError == null && especialidadError == null && doctorError == null) {
                    val nuevaConsulta = Consulta(
                        id = 0,
                        fecha = fecha,
                        hora = hora,
                        especialidad = especialidad,
                        doctor = doctorSeleccionado,
                        paciente = appState.usuarioActual?.nombre ?: "Desconocido"
                    )
                    appState.agregarConsulta(nuevaConsulta)

                    // --- Notificación ---
                    val permissionGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (permissionGranted) {
                        showAgendaNotification(
                            context,
                            "Consulta con $doctorSeleccionado el $fecha a las $hora"
                        )
                    }

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
