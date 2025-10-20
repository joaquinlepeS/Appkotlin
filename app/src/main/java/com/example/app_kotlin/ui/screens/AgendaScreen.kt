package com.example.app_kotlin.ui.screens

import android.os.Build
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.utils.validateFecha
import com.example.app_kotlin.utils.showNotification

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    onNavigateToConsultaCliente: () -> Unit,
    appState: AppState
) {
    val context = LocalContext.current

    var fecha by rememberSaveable { mutableStateOf("") }
    var hora by rememberSaveable { mutableStateOf("") }
    var especialidad by rememberSaveable { mutableStateOf("") }
    var doctorSeleccionado by rememberSaveable { mutableStateOf("") }

    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedDoctor by remember { mutableStateOf(false) }

    var fechaError by remember { mutableStateOf<String?>(null) }
    var horaError by remember { mutableStateOf<String?>(null) }
    var especialidadError by remember { mutableStateOf<String?>(null) }
    var doctorError by remember { mutableStateOf<String?>(null) }

    var notificationPermissionGranted by remember { mutableStateOf(false) }

    // Pide permiso de notificaciones (Android 13+)
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> notificationPermissionGranted = granted }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            else notificationPermissionGranted = true
        } else {
            notificationPermissionGranted = true
        }
    }

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

        // --- Fecha ---
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

        // --- Hora ---
        OutlinedTextField(
            value = hora,
            onValueChange = {
                hora = it
                horaError = if (hora.isBlank()) "La hora no puede estar vacía" else null
            },
            label = { Text("Hora (ej: 15:30)") },
            modifier = Modifier.fillMaxWidth()
        )
        if (horaError != null) Text(horaError!!, color = MaterialTheme.colorScheme.error)

        // --- Especialidad ---
        ExposedDropdownMenuBox(
            expanded = expandedEspecialidad,
            onExpandedChange = { expandedEspecialidad = !expandedEspecialidad }
        ) {
            OutlinedTextField(
                value = especialidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar especialidad") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedEspecialidad) },
                modifier = Modifier
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
                        }
                    )
                }
            }
        }
        if (especialidadError != null) Text(especialidadError!!, color = MaterialTheme.colorScheme.error)

        // --- Doctor ---
        ExposedDropdownMenuBox(
            expanded = expandedDoctor,
            onExpandedChange = { expandedDoctor = !expandedDoctor }
        ) {
            OutlinedTextField(
                value = doctorSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar Doctor") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDoctor) },
                modifier = Modifier
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
                        }
                    )
                }
            }
        }
        if (doctorError != null) Text(doctorError!!, color = MaterialTheme.colorScheme.error)

        // --- Botón Confirmar ---
        Button(
            onClick = {
                fechaError = validateFecha(fecha)
                horaError = if (hora.isBlank()) "La hora no puede estar vacía" else null
                especialidadError = if (especialidad.isBlank()) "Selecciona una especialidad" else null
                doctorError = if (doctorSeleccionado.isBlank()) "Selecciona un doctor" else null

                if (fechaError == null && horaError == null &&
                    especialidadError == null && doctorError == null
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

                    // Mostrar notificación
                    if (notificationPermissionGranted) {
                        showNotification(context, doctorSeleccionado, fecha, hora)
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
            Text("No hay doctores registrados aún.", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        }
    }
}
