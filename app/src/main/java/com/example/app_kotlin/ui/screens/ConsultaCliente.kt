package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.app_kotlin.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaClienteScreen(
    usuarioViewModel: UsuarioViewModel,
    consultaViewModel: ConsultaViewModel,
    onNavigateToAgenda: () -> Unit,
    onNavigateToDoctorList: () -> Unit      // ✔ nuevo parámetro
) {
    val usuarioActual = usuarioViewModel.usuarioActual
    val email = usuarioActual?.email ?: return

    val consultas = consultaViewModel.consultas.collectAsState().value

    LaunchedEffect(Unit) {
        consultaViewModel.cargarConsultas(email)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼 Fondo con imagen igual al login
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Capa oscura para legibilidad
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
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                // 👋 Saludo personalizado
                Text(
                    text = "Hola, ${usuarioActual.nombre} 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Mis Consultas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 🌟 BOTÓN BONITO PARA CONOCER AL PERSONAL
                Button(
                    onClick = onNavigateToDoctorList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text("Conoce a nuestro personal")
                }

                // 📝 Si no hay consultas
                if (consultas.isEmpty()) {
                    Text(
                        text = "No tienes consultas agendadas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
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
    }
}

@Composable
fun ConsultaCard(
    consulta: Consulta,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
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
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text("Eliminar")
            }
        }
    }
}
