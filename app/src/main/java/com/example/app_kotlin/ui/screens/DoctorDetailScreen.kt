package com.example.app_kotlin.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app_kotlin.model.Doctor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.app_kotlin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailScreen(
    doctor: Doctor,
    onAgendar: () -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Información del doctor",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
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

            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.35f))
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AsyncImage(
                        model = doctor.foto,
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )

                    Text(
                        doctor.nombre,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Text(
                        doctor.especialidad,
                        color = Color(0xFFADE2FF),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        "${doctor.experiencia} años de experiencia",
                        color = Color.White
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text("Información de contacto", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))

                            Text("Email: ${doctor.email}")
                            Text("Teléfono: ${doctor.telefono}")

                            Spacer(Modifier.height(12.dp))

                            Text("Ubicación", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))

                            Text("Ciudad: ${doctor.ciudad}")
                            Text("País: ${doctor.pais}")
                        }
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onAgendar
                    ) {
                        Text("Agendar Consulta")
                    }
                }
            }
        }
    }
}
