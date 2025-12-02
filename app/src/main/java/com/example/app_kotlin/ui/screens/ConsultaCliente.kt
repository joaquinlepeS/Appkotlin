package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.R
import com.example.app_kotlin.model.Consulta
import com.example.app_kotlin.viewmodel.ConsultaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaClienteScreen(
    `pacienteViewModel.kt`: `PacienteViewModel.kt`,
    consultaViewModel: ConsultaViewModel,
    onNavigateToAgenda: () -> Unit,
    onNavigateToDoctorList: () -> Unit,
    onNavigateToLogin: () -> Unit

) {
    val usuarioActual = `pacienteViewModel.kt`.pacienteActual
    val email = usuarioActual?.email ?: return

    val consultas = consultaViewModel.consultas.collectAsState().value

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let {
            consultaViewModel.cargarConsultas(it.email)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔵 Fondo estético
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🔵 Capa oscura para legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToAgenda,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agendar nueva consulta")
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                // 👋 Saludo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hola, ${usuarioActual.nombre} 👋",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    IconButton(
                        onClick = {
                            `pacienteViewModel.kt`.logout()

                            onNavigateToLogin() // <-- navega y limpia navegación
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_logout_24),
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }

                }


                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Mis Consultas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 🟣 Botón para ir al personal médico
                Button(
                    onClick = onNavigateToDoctorList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        "Conoce a nuestro personal",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // 📌 Texto si no hay consultas
                if (consultas.isEmpty()) {
                    Text(
                        text = "No tienes consultas agendadas aún.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                } else {
                    // 🟢 Lista de consultas
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
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
    }
}

@Composable
fun ConsultaCard(
    consulta: Consulta,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = consulta.especialidad,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Doctor: ${consulta.doctor}",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Text("Fecha: ${consulta.fecha}")
                Text("Hora: ${consulta.hora}")
            }

            Text(
                text = "Paciente: ${consulta.paciente}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 🔴 Botón eliminar
            Button(
                onClick = onEliminar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Eliminar")
            }
        }
    }
}
