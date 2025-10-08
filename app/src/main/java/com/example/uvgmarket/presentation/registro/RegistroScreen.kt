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
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.constants.ValidationConstants
import com.example.uvgmarket.core.ui.components.buttons.PrimaryButton
import com.example.uvgmarket.core.ui.components.images.CircularImage
import com.example.uvgmarket.core.ui.components.textfields.AppTextField
import com.example.uvgmarket.presentation.theme.AppColors

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

    val validationErrors = remember(
        nombre, usuario, correo, contrasena, confirmarContrasena, showErrors
    ) {
        if (showErrors) {
            getValidationErrors(nombre, usuario, correo, contrasena, confirmarContrasena)
        } else {
            emptyList()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_ingresar_datos),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(UiConstants.PADDING_LARGE.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UiConstants.PADDING_MEDIUM.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(R.string.registro_title),
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp
                )
            }

            item {
                Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                CircularImage(
                    imageRes = R.drawable.avatar_ingresar_datos,
                    contentDescription = "Profile Avatar",
                    size = 150.dp
                )

                Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.nombre_label),
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        showErrors = false
                    },
                    placeholder = stringResource(R.string.registro_nombre_hint),
                    isError = showErrors && nombre.isBlank()
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.usuario_label),
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        showErrors = false
                    },
                    placeholder = stringResource(R.string.registro_usuario_hint),
                    isError = showErrors && usuario.isBlank()
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.correo_label),
                    value = correo,
                    onValueChange = {
                        correo = it
                        showErrors = false
                    },
                    placeholder = stringResource(R.string.registro_correo_hint),
                    isError = showErrors && (!correo.contains("@") || correo.isBlank())
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.contrasena_label),
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        showErrors = false
                    },
                    placeholder = stringResource(R.string.registro_contrasena_hint),
                    isPassword = true,
                    isError = showErrors && contrasena.length < ValidationConstants.MIN_PASSWORD_LENGTH
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.confirmar_contrasena_label),
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

            item {
                Spacer(modifier = Modifier.height(UiConstants.PADDING_SMALL.dp))

                PrimaryButton(
                    text = stringResource(R.string.boton_registrarse),
                    onClick = {
                        if (isValidRegistration(nombre, usuario, correo, contrasena, confirmarContrasena)) {
                            onRegistroClick(nombre, usuario, correo, contrasena)
                        } else {
                            showErrors = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                RegistroFooter(onNavigateToLogin = onNavigateToLogin)
            }

            if (validationErrors.isNotEmpty()) {
                item {
                    ValidationErrorsList(errors = validationErrors)
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun RegistroFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(UiConstants.PADDING_SMALL.dp))

        AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            isPassword = isPassword,
            isError = isError,
            backgroundColor = AppColors.UvgGreenDark,
            textColor = AppColors.TextWhite
        )
    }
}

@Composable
private fun RegistroFooter(onNavigateToLogin: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.ya_tienes_cuenta),
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(UiConstants.PADDING_SMALL.dp))
        Text(
            text = stringResource(R.string.iniciar_sesion_link),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}

@Composable
private fun ValidationErrorsList(errors: List<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = UiConstants.PADDING_MEDIUM.dp)
    ) {
        errors.forEach { error ->
            Text(
                text = "• $error",
                color = Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun isValidRegistration(
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
            contrasena.length >= ValidationConstants.MIN_PASSWORD_LENGTH &&
            contrasena == confirmarContrasena
}

private fun getValidationErrors(
    nombre: String,
    usuario: String,
    correo: String,
    contrasena: String,
    confirmarContrasena: String
): List<String> {
    val errors = mutableListOf<String>()

    if (nombre.isBlank() || usuario.isBlank() || correo.isBlank()) {
        errors.add("Completa todos los campos requeridos")
    }
    if (contrasena.length < ValidationConstants.MIN_PASSWORD_LENGTH) {
        errors.add("La contraseña debe tener al menos ${ValidationConstants.MIN_PASSWORD_LENGTH} caracteres")
    }
    if (!correo.contains("@")) {
        errors.add("El correo electrónico no es válido")
    }
    if (contrasena != confirmarContrasena) {
        errors.add("Las contraseñas no coinciden")
    }

    return errors
}

@Preview
@Composable
fun RegistroScreenPreview() {
    RegistroScreen()
}