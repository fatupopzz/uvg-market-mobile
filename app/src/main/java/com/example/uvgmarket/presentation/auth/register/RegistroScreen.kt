package com.example.uvgmarket.presentation.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.buttons.PrimaryButton
import com.example.uvgmarket.core.ui.components.images.CircularImage
import com.example.uvgmarket.core.ui.components.textfields.AppTextField
import com.example.uvgmarket.presentation.auth.register.RegisterViewModel
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    // Observar el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Manejar el éxito del registro
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegistroSuccess()
            viewModel.resetState()
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
                    color = MaterialTheme.colorScheme.onPrimary,
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
                        viewModel.clearError()
                    },
                    placeholder = stringResource(R.string.registro_nombre_hint),
                    isError = false,
                    enabled = !uiState.isLoading
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.usuario_label),
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        viewModel.clearError()
                    },
                    placeholder = stringResource(R.string.registro_usuario_hint),
                    isError = false,
                    enabled = !uiState.isLoading
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.correo_label),
                    value = correo,
                    onValueChange = {
                        correo = it
                        viewModel.clearError()
                    },
                    placeholder = stringResource(R.string.registro_correo_hint),
                    isError = false,
                    enabled = !uiState.isLoading
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.contrasena_label),
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        viewModel.clearError()
                    },
                    placeholder = stringResource(R.string.registro_contrasena_hint),
                    isPassword = true,
                    isError = false,
                    enabled = !uiState.isLoading
                )
            }

            item {
                RegistroFormField(
                    label = stringResource(R.string.confirmar_contrasena_label),
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        viewModel.clearError()
                    },
                    placeholder = stringResource(R.string.registro_confirmar_hint),
                    isPassword = true,
                    isError = false,
                    enabled = !uiState.isLoading
                )
            }

            item {
                Spacer(modifier = Modifier.height(UiConstants.PADDING_SMALL.dp))

                PrimaryButton(
                    text = if (uiState.isLoading) "Creando cuenta..." else stringResource(R.string.boton_registrarse),
                    onClick = {
                        viewModel.register(nombre, usuario, correo, contrasena, confirmarContrasena)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                )
            }

            // Mensaje de estado durante el registro
            if (uiState.isLoading) {
                item {
                    Text(
                        text = "Creando tu cuenta en Firebase...\nEsto puede tardar unos segundos.",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = UiConstants.PADDING_SMALL.dp)
                    )
                }
            }

            item {
                RegistroFooter(onNavigateToLogin = onNavigateToLogin)
            }

            // Mostrar error si existe
            if (uiState.error != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = UiConstants.PADDING_MEDIUM.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(UiConstants.PADDING_MEDIUM.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Indicador de carga
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 6.dp
                )
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
    isError: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(UiConstants.PADDING_SMALL.dp))

        AppTextField(
            value = value,
            onValueChange = if (enabled) onValueChange else { _ -> },
            placeholder = placeholder,
            isPassword = isPassword,
            isError = isError,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.fillMaxWidth()
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
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(UiConstants.PADDING_SMALL.dp))
        Text(
            text = stringResource(R.string.iniciar_sesion_link),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onNavigateToLogin() }
        )
    }
}

@Preview
@Composable
fun RegistroScreenPreview() {
    UvgMarketTheme {
        RegistroScreen()
    }
}