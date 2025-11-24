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
import com.example.app_kotlin.viewmodel.UsuarioViewModel
import com.example.app_kotlin.viewmodel.DoctorViewModel
import com.google.gson.Gson

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    // ViewModels globales para que sobrevivan entre pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val consultaViewModel: ConsultaViewModel = viewModel()
    val doctorViewModel: DoctorViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN
    ) {

        // LOGIN
        composable(Screens.LOGIN) {
            LoginScreen(
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
                onNavigateToAgenda = { navController.navigate(Screens.AGENDA) },
                onNavigateToLogin = {
                    usuarioViewModel.logout()
                    navController.navigate(Screens.LOGIN) {
                        popUpTo(0)
                    }
                },
                onNavigateToDoctorList = { navController.navigate(Screens.DOCTOR_LIST) }
            )
        }

        // AGENDA (PACIENTE)
        composable(Screens.AGENDA) {
            AgendaScreen(
                usuarioViewModel = usuarioViewModel,
                consultaViewModel = consultaViewModel,
                doctorViewModel = doctorViewModel,
                onNavigateToConsultaCliente = { navController.navigate(Screens.CONSULTACLIENTE) }
            )
        }

        // LISTA DE DOCTORES (API)
        composable(Screens.DOCTOR_LIST) {
            DoctorListScreen(
                onDoctorSelected = { doctorJson ->
                    navController.navigate("${Screens.DOCTOR_DETAIL}/$doctorJson")
                }
            )
        }

        // DETALLE DEL DOCTOR
        composable(
            route = Screens.DOCTOR_DETAIL + "/{doctorJson}",
            arguments = listOf(navArgument("doctorJson") { type = NavType.StringType })
        ) { backStackEntry ->

            val doctorJson = backStackEntry.arguments?.getString("doctorJson")!!
            val doctor = Gson().fromJson(doctorJson, Doctor::class.java)

            DoctorDetailScreen(
                doctor = doctor,
                onAgendar = {
                    navController.navigate(Screens.AGENDA)
                }
            )
        }
    }
}
