package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

        // Imagen centrada
        AsyncImage(
            model = doctor.foto,
            contentDescription = "Doctor image",
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre + especialidad
        Text(
            text = doctor.nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = doctor.especialidad,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${doctor.experiencia} años de experiencia",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Información adicional dentro de una Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Información de contacto", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Email: ${doctor.email}")
                Text("Teléfono: ${doctor.telefono}")

                Spacer(modifier = Modifier.height(12.dp))
                Text("Ubicación", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))
                Text("Ciudad: ${doctor.ciudad}")
                Text("País: ${doctor.pais}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAgendar
        ) {
            Text("Agendar Hora")
        }
    }
}
