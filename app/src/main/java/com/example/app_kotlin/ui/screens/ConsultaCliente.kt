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
import androidx.compose.ui.tooling.preview.Preview
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

        // 🌄 Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🩺 Estructura principal con TopBar y FAB
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToAgendaScreen },
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
                            // Botón Cerrar sesión
                            TextButton(
                                onClick = {
                                    appState.logout()   // llama a tu función logout
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

                            // Logo + texto
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

            // 🎨 Fondo oscuro translúcido y contenido centrado
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {

                    // 🩷 Primera sección con su color propio
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
                                text = "Bienvenido a tu consulta",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(5) { index ->
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
                                            items(consultas.size) { index ->
                                                val consulta = consultas[index]
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
                                                        Text(
                                                            text = consulta,
                                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                            style = MaterialTheme.typography.bodyMedium
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

            // 💙 Segunda sección con color distinto
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ),
                modifier = Modifier
                    .height(120.dp)
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
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(5) { index ->
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
                                    items(doctores.size) { index ->
                                        val doc = doctores[index]
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
                                                Text(
                                                    text = doc.nombre,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                    style = MaterialTheme.typography.bodyMedium
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



