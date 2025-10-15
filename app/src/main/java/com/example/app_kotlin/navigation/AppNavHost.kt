package com.example.app_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_kotlin.ui.screens.LoginScreen
import com.example.app_kotlin.ui.screens.RegistroScreen


@Composable
fun AppNavHost() {
    val navController = rememberNavController() // ahora navController está dentro de un Composable

    NavHost(navController = navController, startDestination = Screens.LOGIN) {
        composable(Screens.LOGIN) {
            LoginScreen(
                onNavigateToRegistro = { navController.navigate(Screens.REGISTER) },

            )
        }
        composable(Screens.REGISTER) {
            RegistroScreen(onNavigateToLogin = { navController.navigate(Screens.LOGIN) })
        }

    }
}