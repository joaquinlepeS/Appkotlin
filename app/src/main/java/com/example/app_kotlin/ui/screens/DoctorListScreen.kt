package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.google.gson.Gson
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DoctorListScreen(
    doctorViewModel: DoctorViewModel,     // ✔ ViewModel global (se mantiene igual)
    onDoctorSelected: (String) -> Unit
) {
    val doctors = doctorViewModel.doctors
    val isLoading = doctorViewModel.isLoading
    val error = doctorViewModel.errorMessage

    println("DEBUG → DoctorListScreen CARGADA, doctorViewModel = $doctorViewModel")

    // Ejecutamos la carga una vez
    LaunchedEffect(Unit) {
        println("DEBUG → Ejecutando fetchDoctors()")
        doctorViewModel.fetchDoctors()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Nuestro equipo médico",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error")
                }
            }

            else -> {
                if (doctors.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se encontraron doctores.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(doctors) { doctor ->
                            DoctorCard(
                                doctor = doctor,
                                onClick = {
                                    // Convertir doctor a JSON (se mantiene igual)
                                    val doctorJson = Gson().toJson(doctor)
                                    onDoctorSelected(doctorJson)
                                }
                            )
                        }
                    }
                }
            }
        }
        Button(onClick = { doctorViewModel.subirDoctoresAMockApi() }) {
            Text("Subir doctores a MockAPI")
        }

    }
}

@Composable
fun DoctorCard(
    doctor: Doctor,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Foto redonda del doctor
            AsyncImage(
                model = doctor.foto,
                contentDescription = "Foto del doctor",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                // Nombre del doctor
                Text(
                    text = doctor.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Especialidad como "pill"
                AssistChip(
                    onClick = { /* solo decorativo */ },
                    label = {
                        Text(
                            text = doctor.especialidad,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )

                // Ciudad y país
                Text(
                    text = "${doctor.ciudad}, ${doctor.pais}",
                    style = MaterialTheme.typography.bodySmall
                )

                // Años de experiencia
                Text(
                    text = "${doctor.experiencia} años de experiencia",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
