package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.R
import com.example.app_kotlin.viewmodel.UsuarioViewModel
import com.example.app_kotlin.viewmodel.ConsultaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaClienteScreen(
    usuarioViewModel: UsuarioViewModel,
    consultaViewModel: ConsultaViewModel,
    onNavigateToAgenda: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToDoctorList: () -> Unit
) {
    val usuario = usuarioViewModel.usuarioActual
    val consultas = consultaViewModel.consultas

    // cargar consultas al entrar
    LaunchedEffect(usuario) {
        usuario?.email?.let { consultaViewModel.cargarConsultas(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToAgenda() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = "Agendar consulta"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            TextButton(onClick = {
                                usuarioViewModel.logout()
                                onNavigateToLogin()
                            }) {
                                Text(
                                    text = "Cerrar sesión",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Image(
                                painter = painterResource(id = R.drawable.baseline_medical_services_24),
                                contentDescription = "Logo",
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "ConsultaMed",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp)
                ) {

                    // --- TITULO ---
                    Card(
                        colors = CardDefaults.cardColors(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = "Bienvenido, ${usuario?.nombre ?: "Usuario"}",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )

                            // --- CONSULTAS ---
                            if (consultas.isEmpty()) {
                                Text(
                                    text = "Aún no tienes consultas registradas.",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(consultas.size) { idx ->
                                        val consulta = consultas[idx]

                                        Card(
                                            modifier = Modifier
                                                .width(220.dp)
                                                .height(150.dp)
                                                .padding(vertical = 8.dp),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                verticalArrangement = Arrangement.SpaceEvenly
                                            ) {

                                                Text(
                                                    text = "Consulta ID: ${consulta.id}",
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )

                                                Text(
                                                    text = "Fecha: ${consulta.fecha}",
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )

                                                Text(
                                                    text = "Hora: ${consulta.hora}",
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )

                                                Text(
                                                    text = "Especialidad: ${consulta.especialidad}",
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // --- BOTÓN VER DOCTORES ---
                    Button(onClick = { onNavigateToDoctorList() }) {
                        Text("Ver Doctores")
                    }

                }
            }
        }
    }
}
