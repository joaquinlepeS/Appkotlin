package com.example.app_kotlin.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.app_kotlin.R
import com.example.app_kotlin.utils.validateEmail
import com.example.app_kotlin.utils.validateLogin
import com.example.app_kotlin.utils.validatePassword
import com.example.app_kotlin.utils.validateRegistro
import com.example.app_kotlin.utils.validateUsuario
import com.example.app_kotlin.utils.validateRepetirPassword



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegistroScreen(onNavigateToLogin: () -> Unit ){

    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    // Estados de errores
    var usuarioError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var password1Error by remember { mutableStateOf<String?>(null) }
    var password2Error by remember { mutableStateOf<String?>(null) }

    var registroExitoso by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.wallpaper ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.baseline_medical_services_24),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp) // espacio entre imagen y texto
                            )

                            Text(
                                text = "ConsultaMed",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },

            ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                .background(Color.Black.copy(alpha = 0.3f)) ,
                contentAlignment = Alignment.Center,

                ) {
                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSurfaceVariant),
                    border = BorderStroke(2.dp, Color.Transparent), // grosor y color del borde
                    elevation = CardDefaults.cardElevation(40.dp),
                    modifier = Modifier.padding( 24.dp)




                    ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                    {
                        Text(
                            text = "Registrate",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,


                            )
                        OutlinedTextField(
                            value = usuario,
                            onValueChange = {
                                usuario = it
                                usuarioError = validateUsuario(usuario) // validación en tiempo real
                            },
                            label = { Text("Usuario") },
                            singleLine = true,
                            modifier = Modifier.padding(3.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        if (usuarioError != null) {
                            Text(
                                text = usuarioError!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = validateEmail(email) // validación en tiempo real
                            },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.padding(3.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = password1,
                            onValueChange = {
                                password1 = it
                                password1Error = validatePassword(password1)
                            },
                            label = { Text("Ingrese Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.padding(3.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer

                            )
                        )

                        if (password1Error != null) {
                            Text(
                                text = password1Error!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = password2,
                            onValueChange = {
                                password2 = it
                                password2Error = validateRepetirPassword(password1,password2)
                            },
                            label = { Text("Repita Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.padding(3.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedLabelColor = MaterialTheme.colorScheme.primaryContainer

                            )
                        )

                        if (password2Error != null) {
                            Text(
                                text = password2Error!!,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }


                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {

                                val errors = validateRegistro(usuario,email, password1,password2)
                                usuarioError = errors.usuarioError
                                emailError = errors.emailError
                                password1Error = errors.password1Error
                                password2Error= errors.password2Error

                                if (errors.emailError == null && errors.password1Error == null
                                    && errors.password2Error == null && errors.usuarioError == null) {
                                    registroExitoso = true
                                }
                            },
                            modifier = Modifier.padding(6.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text("Registrarse",
                                color = MaterialTheme.colorScheme.onSurface )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = registroExitoso,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(24.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(24.dp)
                        .zIndex(2f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¡Registro exitoso!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary  // verde elegante
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            registroExitoso = false
                            onNavigateToLogin()
                        },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Aceptar")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun previewlogin(){
    RegistroScreen {  }
}