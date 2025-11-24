package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaClienteScreen(
    usuarioViewModel: UsuarioViewModel,
    consultaViewModel: ConsultaViewModel
) {
    val usuarioActual = usuarioViewModel.usuarioActual
    val email = usuarioActual?.email ?: return

    // cargar consultas del usuario
    val consultas = consultaViewModel.consultas.collectAsState().value

    LaunchedEffect(Unit) {
        consultaViewModel.cargarConsultas(email)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis Consultas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (consultas.isEmpty()) {
            Text(
                text = "No tienes consultas agendadas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(consultas) { consulta ->
                    ConsultaCard(
                        consulta = consulta,
                        onEliminar = {
                            consultaViewModel.eliminarConsulta(email, consulta.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConsultaCard(
    consulta: Consulta,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = consulta.especialidad,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Doctor: ${consulta.doctor}")
            Text("Fecha: ${consulta.fecha}")
            Text("Hora: ${consulta.hora}")
            Text("Paciente: ${consulta.paciente}")

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEliminar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        }
    }
}
