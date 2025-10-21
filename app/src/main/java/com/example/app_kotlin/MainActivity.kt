package com.example.app_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.DataStoreManager
import com.example.app_kotlin.navigation.AppNavHost
import com.example.logincompose.ui.theme.LoginComposeTheme
import com.example.app_kotlin.ui.screens.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Crear el DataStoreManager
                    val dataStore = remember { DataStoreManager(context = applicationContext) }
                    // Crear AppState una sola vez
                    var appState = remember { AppState(dataStore) }

                    // Cargar datos al iniciar
                    LaunchedEffect (Unit) {
                        appState.cargarDatos()

                    }

                    // Pasar appState al NavHost
                    AppNavHost(appState = appState)
                }
            }
        }
    }
}

