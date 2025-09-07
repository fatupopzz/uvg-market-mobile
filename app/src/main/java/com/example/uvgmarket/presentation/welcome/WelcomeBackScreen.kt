package com.example.uvgmarket.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.R
import com.example.uvgmarket.presentation.components.*

@Composable
fun WelcomeBackScreen(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.background_ingresar_datos),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))

                // Título
                Text(
                    text = stringResource(R.string.welcome_back_title),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp
                )
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))

                // Avatar
                ProfileImage(modifier = Modifier.size(170.dp))

                Spacer(modifier = Modifier.height(50.dp))
            }

            // Campo Usuario
            item {
                Column {
                    Text(
                        text = stringResource(R.string.usuario_label),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = usuario,
                        onValueChange = {
                            usuario = it
                            showErrors = false
                            showSuccess = false
                        },
                        placeholder = stringResource(R.string.welcome_usuario_hint),
                        isError = showErrors && usuario.isBlank()
                    )
                }
            }

            // Campo Contraseña
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    Text(
                        text = stringResource(R.string.contrasena_label),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            showErrors = false
                            showSuccess = false
                        },
                        placeholder = stringResource(R.string.welcome_contrasena_hint),
                        isPassword = true,
                        isError = showErrors && contrasena.isBlank()
                    )
                }
            }

            // Botón Iniciar Sesión
            item {
                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    text = stringResource(R.string.boton_iniciar_sesion),
                    onClick = {
                        if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                            onLoginClick(usuario, contrasena)
                            showSuccess = true
                            showErrors = false
                        } else {
                            showErrors = true
                            showSuccess = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Link a Registro
            item {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.no_tienes_cuenta),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.crear_cuenta_link),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }

            // Mensaje de éxito
            if (showSuccess) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.login_exitoso),
                        color = Color.Green,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Mensaje de error
            if (showErrors) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Por favor completa todos los campos",
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Preview
@Composable
fun WelcomeBackScreenPreview() {
    WelcomeBackScreen()
}