package com.example.app_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.ui.screens.AgendaScreen
import com.example.app_kotlin.ui.screens.ConsultaClienteScreen
import com.example.app_kotlin.ui.screens.LoginScreen
import com.example.app_kotlin.ui.screens.RegistroScreen


@Composable
fun AppNavHost(appState: AppState) {
    val navController = rememberNavController() // ahora navController está dentro de un Composable

    NavHost(navController = navController, startDestination = Screens.LOGIN) {
        composable(Screens.LOGIN) {
            LoginScreen(
                onNavigateToRegistro = { navController.navigate(Screens.REGISTER) },
                onNavigateToConsultaCliente = {navController.navigate(Screens.CONSULTACLIENTE)},
                appState = appState


            )
        }
        composable(Screens.REGISTER) {
            RegistroScreen(
                onNavigateToLogin = { navController.navigate(Screens.LOGIN) },
                appState = appState)


        }

        composable(Screens.CONSULTACLIENTE){
            ConsultaClienteScreen(
                onNavigateToAgendaScreen = {navController.navigate(Screens.AGENDA)},
                onNavigateToLoginScreen = {navController.navigate(Screens.LOGIN)},
                appState = appState)
        }

        composable(Screens.AGENDA)  {
            AgendaScreen(onNavigateToConsultaCliente = {navController.navigate(Screens.CONSULTACLIENTE)},
                appState = appState)
        }

    }
}