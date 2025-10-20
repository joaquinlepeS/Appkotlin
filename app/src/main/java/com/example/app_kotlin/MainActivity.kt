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
import com.example.app_kotlin.model.AppState
import com.example.app_kotlin.model.DataStoreManager
import com.example.app_kotlin.navigation.AppNavHost
import com.example.logincompose.ui.theme.LoginComposeTheme

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
                    val dataStore = remember { DataStoreManager(applicationContext) }
                    val appState = remember { AppState(dataStore) }

                    LaunchedEffect(Unit) {
                        appState.cargarDatos()
                    }

                    AppNavHost(appState = appState)
                }
            }
        }
    }
}
