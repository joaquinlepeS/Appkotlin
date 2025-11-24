package com.example.app_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app_kotlin.model.Doctor
import com.example.app_kotlin.ui.screens.*
import com.example.app_kotlin.viewmodel.ConsultaViewModel
import com.example.app_kotlin.viewmodel.PacienteViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.google.gson.Gson
import android.util.Base64   // ✔ Base64 válido para todas las versiones de Android

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    // ViewModels globales
    val usuarioViewModel: PacienteViewModel = viewModel()
    val consultaViewModel: ConsultaViewModel = viewModel()
    val doctorViewModel: DoctorViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN
    ) {

        // LOGIN
        composable(Screens.LOGIN) {
            LoginScreen(
                usuarioViewModel = usuarioViewModel,
                onNavigateToRegistro = { navController.navigate(Screens.REGISTER) },
                onNavigateToConsultaCliente = {
                    navController.navigate(Screens.CONSULTACLIENTE) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // REGISTRO
        composable(Screens.REGISTER) {
            RegistroScreen(
                onNavigateToLogin = { navController.navigate(Screens.LOGIN) }
            )
        }

        // CONSULTA CLIENTE
        composable(Screens.CONSULTACLIENTE) {
            ConsultaClienteScreen(
                usuarioViewModel = usuarioViewModel,
                consultaViewModel = consultaViewModel,
                onNavigateToAgenda = {
                    navController.navigate(Screens.AGENDA)
                },
                onNavigateToDoctorList = {
                    navController.navigate(Screens.DOCTOR_LIST)
                },
                onNavigateToLogin = {
                    navController.navigate(Screens.LOGIN) {
                        popUpTo(0) { inclusive = true } // Limpia TODO el backstack
                    }
                }
            )



        }

        // AGENDA
        composable(Screens.AGENDA) {
            AgendaScreen(
                usuarioViewModel = usuarioViewModel,
                consultaViewModel = consultaViewModel,
                doctorViewModel = doctorViewModel,
                onNavigateToConsultaCliente = {
                    navController.navigate(Screens.CONSULTACLIENTE)
                }
            )
        }

        // LISTA DE DOCTORES
        composable(Screens.DOCTOR_LIST) {
            DoctorListScreen(
                doctorViewModel = doctorViewModel,
                onDoctorSelected = { doctorJson ->

                    // ✔ Base64 compatible para todas las APIs
                    val encoded = Base64.encodeToString(
                        doctorJson.toByteArray(),
                        Base64.URL_SAFE or Base64.NO_WRAP
                    )

                    navController.navigate("${Screens.DOCTOR_DETAIL}/$encoded")
                }
            )
        }

        // DETALLE DEL DOCTOR
        composable(
            route = Screens.DOCTOR_DETAIL + "/{doctorJson}",
            arguments = listOf(navArgument("doctorJson") { type = NavType.StringType })
        ) { backStackEntry ->

            val encoded = backStackEntry.arguments?.getString("doctorJson")!!

            // ✔ Decodificación segura
            val decodedBytes = Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_WRAP)
            val decodedJson = String(decodedBytes)

            val doctor = Gson().fromJson(decodedJson, Doctor::class.java)

            DoctorDetailScreen(
                doctor = doctor,
                onAgendar = { navController.navigate(Screens.AGENDA) }
            )
        }
    }
}
