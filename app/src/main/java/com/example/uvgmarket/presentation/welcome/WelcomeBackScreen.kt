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
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.buttons.PrimaryButton
import com.example.uvgmarket.core.ui.components.images.CircularImage
import com.example.uvgmarket.core.ui.components.textfields.AppTextField
import com.example.uvgmarket.presentation.theme.AppColors

@Composable
fun WelcomeBackScreen(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onNavigateToRegister: () -> Unit = {}
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

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
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))

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

                CircularImage(
                    imageRes = R.drawable.avatar_ingresar_datos,
                    contentDescription = "Profile Avatar",
                    size = UiConstants.AVATAR_SIZE_EXTRA_LARGE.dp
                )

                Spacer(modifier = Modifier.height(50.dp))
            }

            item {
                LoginFormField(
                    label = stringResource(R.string.usuario_label),
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        showErrors = false
                    },
                    placeholder = stringResource(R.string.welcome_usuario_hint),
                    isError = showErrors && usuario.isBlank()
                )
            }

            item {
                Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                LoginFormField(
                    label = stringResource(R.string.contrasena_label),
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        showErrors = false
                    },
                    placeholder = stringResource(R.string.welcome_contrasena_hint),
                    isPassword = true,
                    isError = showErrors && contrasena.isBlank()
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))

                PrimaryButton(
                    text = stringResource(R.string.boton_iniciar_sesion),
                    onClick = {
                        if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                            onLoginClick(usuario, contrasena)
                        } else {
                            showErrors = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(UiConstants.PADDING_EXTRA_LARGE.dp))

                LoginFooter(onNavigateToRegister = onNavigateToRegister)
            }

            if (showErrors) {
                item {
                    Spacer(modifier = Modifier.height(UiConstants.PADDING_MEDIUM.dp))
                    ErrorMessage(text = "Por favor completa todos los campos")
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun LoginFormField(
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
private fun LoginFooter(onNavigateToRegister: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_tienes_cuenta),
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(UiConstants.PADDING_SMALL.dp))
        Text(
            text = stringResource(R.string.crear_cuenta_link),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onNavigateToRegister() }
        )
    }
}

@Composable
private fun ErrorMessage(text: String) {
    Text(
        text = text,
        color = Color.Red,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun WelcomeBackScreenPreview() {
    WelcomeBackScreen()
}