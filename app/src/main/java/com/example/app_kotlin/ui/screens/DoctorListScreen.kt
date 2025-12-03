package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.google.gson.Gson
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.app_kotlin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorListScreen(
    doctorViewModel: DoctorViewModel,
    onDoctorSelected: (String) -> Unit
) {
    val doctors = doctorViewModel.doctors
    val isLoading = doctorViewModel.isLoading
    val error = doctorViewModel.errorMessage

    LaunchedEffect(Unit) { doctorViewModel.fetchDoctors() }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nuestro equipo médico",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {

            // Fondo igual que Agenda y Login
            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(padding)
            ) {

                when {
                    isLoading -> {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }

                    error != null -> {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text("Error: $error", color = Color.White)
                        }
                    }

                    doctors.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text("No se encontraron doctores.", color = Color.White)
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(doctors) { doctor ->
                                DoctorCard(
                                    doctor = doctor,
                                    onClick = {
                                        val doctorJson = Gson().toJson(doctor)
                                        onDoctorSelected(doctorJson)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = doctor.foto,
                contentDescription = "Foto",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = doctor.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text(doctor.especialidad)
                    }
                )

                Text("${doctor.ciudad}, ${doctor.pais}", color = Color.Gray)
                Text("${doctor.experiencia} años de experiencia", color = Color.Gray)
            }
        }
    }
}
