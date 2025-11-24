package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
import com.example.app_kotlin.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaClienteScreen(
    usuarioViewModel: UsuarioViewModel,
    consultaViewModel: ConsultaViewModel,
    onNavigateToAgenda: () -> Unit,
    onNavigateToDoctorList: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val usuarioActual = usuarioViewModel.usuarioActual
    val email = usuarioActual?.email ?: return

    val consultas = consultaViewModel.consultas.collectAsState().value

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let {
            consultaViewModel.cargarConsultas(it.email)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔵 Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🔵 Capa oscura para mejorar contraste
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

            // 🌟 TODO EL CONTENIDO AHORA ES SCROLLABLE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {

                // 👋 Saludo + Logout
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
                            usuarioViewModel.logout()
                            onNavigateToLogin()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_logout_24),
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Mis Consultas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 📅 Lista de consultas
                if (consultas.isEmpty()) {
                    Text(
                        text = "No tienes consultas agendadas aún.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                } else {
                    consultas.forEach { consulta ->
                        ConsultaCard(
                            consulta = consulta,
                            onEliminar = {
                                consultaViewModel.eliminarConsulta(email, consulta.id)
                            }
                        )

                        Spacer(Modifier.height(14.dp))
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 🔥 BOTÓN FINAL DE LA PANTALLA (SIEMPRE ABAJO)
                Button(
                    onClick = onNavigateToDoctorList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
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
            }
        }
    }
}

@Composable
fun ConsultaCard(
    consulta: Consulta,
    onEliminar: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Black.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Especialidad: ${consulta.especialidad}", style = MaterialTheme.typography.titleMedium)
            Text("Doctor: ${consulta.doctor}", style = MaterialTheme.typography.bodyLarge)
            Text("Fecha: ${consulta.fecha}", style = MaterialTheme.typography.bodyLarge)
            Text("Hora: ${consulta.hora}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEliminar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Eliminar consulta")
            }
        }
    }
}
