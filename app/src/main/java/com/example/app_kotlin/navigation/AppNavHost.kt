package com.example.app_kotlin.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    // ViewModels globales (se mantienen entre pantallas)
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
                consultaViewModel = consultaViewModel
            )
        }

        // AGENDA
        composable(Screens.AGENDA) {
            AgendaScreen(
                usuarioViewModel = usuarioViewModel,
                consultaViewModel = consultaViewModel,
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

                    // → protegemos el JSON para que no rompa la navegación
                    val encoded = Base64.getUrlEncoder().encodeToString(doctorJson.toByteArray())

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
            val decodedJson = String(Base64.getUrlDecoder().decode(encoded))
            val doctor = Gson().fromJson(decodedJson, Doctor::class.java)

            DoctorDetailScreen(
                doctor = doctor,
                onAgendar = { navController.navigate(Screens.AGENDA) }
            )
        }
    }
}
