package com.example.uvgmarket.presentation.registro

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
fun RegistroScreen(
    onRegistroClick: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onNavigateToLogin: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))

                // Título
                Text(
                    text = stringResource(R.string.registro_title),
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                // Avatar
                ProfileImage(modifier = Modifier.size(150.dp))

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Campo Nombre
            item {
                Column {
                    Text(
                        text = stringResource(R.string.nombre_label),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            showErrors = false
                        },
                        placeholder = stringResource(R.string.registro_nombre_hint),
                        isError = showErrors && nombre.isBlank()
                    )
                }
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
                        },
                        placeholder = stringResource(R.string.registro_usuario_hint),
                        isError = showErrors && usuario.isBlank()
                    )
                }
            }

            // Campo Correo
            item {
                Column {
                    Text(
                        text = stringResource(R.string.correo_label),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            showErrors = false
                        },
                        placeholder = stringResource(R.string.registro_correo_hint),
                        isError = showErrors && (!correo.contains("@") || correo.isBlank())
                    )
                }
            }

            // Campo Contraseña
            item {
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
                        },
                        placeholder = stringResource(R.string.registro_contrasena_hint),
                        isPassword = true,
                        isError = showErrors && contrasena.length < 8
                    )
                }
            }

            // Campo Confirmar Contraseña
            item {
                Column {
                    Text(
                        text = stringResource(R.string.confirmar_contrasena_label),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            showErrors = false
                        },
                        placeholder = stringResource(R.string.registro_confirmar_hint),
                        isPassword = true,
                        isError = showErrors && confirmarContrasena != contrasena
                    )
                }
            }

            // Botón Registrarse
            item {
                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.boton_registrarse),
                    onClick = {
                        if (validarRegistro(nombre, usuario, correo, contrasena, confirmarContrasena)) {
                            onRegistroClick(nombre, usuario, correo, contrasena)
                            showSuccess = true
                        } else {
                            showErrors = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Link a Login
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.ya_tienes_cuenta),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.iniciar_sesion_link),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }

            // Mensajes de éxito y error
            if (showSuccess) {
                item {
                    Text(
                        text = stringResource(R.string.registro_exitoso),
                        color = Color.Green,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (showErrors) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (nombre.isBlank() || usuario.isBlank() || correo.isBlank()) {
                            Text(
                                text = "• Completa todos los campos requeridos",
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (contrasena.length < 8) {
                            Text(
                                text = "• ${stringResource(R.string.error_contrasena_corta)}",
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (!correo.contains("@")) {
                            Text(
                                text = "• ${stringResource(R.string.error_correo_invalido)}",
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (contrasena != confirmarContrasena) {
                            Text(
                                text = "• ${stringResource(R.string.error_contrasenas_no_coinciden)}",
                                color = Color.Red,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

private fun validarRegistro(
    nombre: String,
    usuario: String,
    correo: String,
    contrasena: String,
    confirmarContrasena: String
): Boolean {
    return nombre.isNotBlank() &&
            usuario.isNotBlank() &&
            correo.isNotBlank() &&
            correo.contains("@") &&
            contrasena.length >= 8 &&
            contrasena == confirmarContrasena
}

@Preview
@Composable
fun RegistroScreenPreview() {
    RegistroScreen()
}