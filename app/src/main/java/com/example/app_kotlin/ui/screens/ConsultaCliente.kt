package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.R
import com.example.app_kotlin.model.AppState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaClienteScreen(
    onNavigateToAgendaScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit,
    appState: AppState
) {
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
                    onClick = { onNavigateToAgendaScreen() }, // << corregido: llamar la lambda
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
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    appState.logout()
                                    onNavigateToLoginScreen()
                                }
                            ) {
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
                    modifier = Modifier.fillMaxSize().padding(vertical = 24.dp)
                ) {

                    // --- SECCIÓN CONSULTAS ---
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = "Bienvenido, ${appState.usuarioActual?.nombre ?: "Usuario"} a tu consulta",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(all = 12.dp),
                                textAlign = TextAlign.Center
                            )

                            val consultas = appState.obtenerConsultas()

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
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(8.dp),
                                                    verticalArrangement = Arrangement.SpaceEvenly,
                                                    horizontalAlignment = Alignment.Start
                                                ) {
                                                    Text(
                                                        text = "consulta",
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        text = "ID: ${consulta.id}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                    Text(
                                                        text = "Fecha: ${consulta.fecha}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                    Text(
                                                        text = "Hora: ${consulta.hora}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                    Text(
                                                        text = "Especialidad: ${consulta.especialidad}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                    // --- SECCIÓN DOCTORES ---
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = "Conoce a nuestro equipo",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            val doctores = appState.doctores

                            if (doctores.isEmpty()) {
                                Text(
                                    text = "Aún no hay doctores registrados.",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(doctores.size) { idx ->
                                        val doc = doctores[idx]
                                        Card(
                                            modifier = Modifier
                                                .width(220.dp)
                                                .height(150.dp)
                                                .padding(vertical = 8.dp),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.fillMaxSize()
                                            ){
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                ){
                                                Image(
                                                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                                        contentDescription = "Logo",
                                                        modifier = Modifier.size(100.dp)
                                                    )
                                                    Text(
                                                        text = doc.nombre,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                        style = MaterialTheme.typography.titleLarge
                                                    )

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
