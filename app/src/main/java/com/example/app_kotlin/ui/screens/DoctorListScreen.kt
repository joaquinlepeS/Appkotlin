package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.viewmodel.DoctorViewModel

@Composable
fun DoctorListScreen(
    onDoctorSelected: (Doctor) -> Unit,
    viewModel: DoctorViewModel = viewModel()
) {
    val doctors = viewModel.doctors
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    // Ejecutar fetchDoctors() solo UNA vez
    LaunchedEffect(Unit) {
        viewModel.fetchDoctors()
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(text = "Error: $error")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(doctors) { doctor ->
                    DoctorCard(doctor) {
                        onDoctorSelected(doctor)
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
            .padding(bottom = 16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            AsyncImage(
                model = doctor.picture,
                contentDescription = "Doctor picture",
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            Column {

                Text(text = doctor.name, style = MaterialTheme.typography.titleMedium)
                Text(text = doctor.specialty, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${doctor.yearsExperience} años de experiencia", style = MaterialTheme.typography.bodySmall)
                Text(text = "${doctor.city}, ${doctor.country}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
