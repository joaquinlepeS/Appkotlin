package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_kotlin.model.Doctor

@Composable
fun DoctorDetailScreen(
    doctor: Doctor,
    onAgendar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        AsyncImage(
            model = doctor.foto,
            contentDescription = "Doctor image",
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = doctor.nombre, style = MaterialTheme.typography.headlineSmall)
        Text(text = doctor.especialidad, style = MaterialTheme.typography.titleMedium)
        Text(text = "${doctor.experiencia} años de experiencia")

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Email: ${doctor.email}")
        Text(text = "Teléfono: ${doctor.telefono}")
        Text(text = "Ciudad: ${doctor.ciudad}")
        Text(text = "País: ${doctor.pais}")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAgendar
        ) {
            Text("Agendar Hora")
        }
    }
}
