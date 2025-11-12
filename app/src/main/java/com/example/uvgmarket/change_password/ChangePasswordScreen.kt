package com.example.uvgmarket.change_password


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.change_password.components.PasswordChangeTopBar
import com.example.uvgmarket.change_password.components.PasswordTextField
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.buttons.SecondaryButton
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit = {},
    onPasswordChanged: () -> Unit = {},
    viewModel: ChangePasswordViewModel = viewModel()
) {
    var contrasenaActual by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    // Observar el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Manejar el éxito del cambio de contraseña
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onPasswordChanged()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            item {
                PasswordChangeTopBar(onBackClick = onBackClick)
            }

            // Contenido del formulario
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UiConstants.PADDING_LARGE.dp)
                        .padding(top = UiConstants.PADDING_EXTRA_LARGE.dp)
                ) {
                    // Campo: Contraseña actual
                    PasswordTextField(
                        label = stringResource(R.string.Cambiar_contrasena_actual),
                        value = contrasenaActual,
                        onValueChange = {
                            contrasenaActual = it
                            viewModel.clearError()
                        },
                        isError = uiState.validationErrors.isNotEmpty() && contrasenaActual.isBlank(),
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    // Campo: Nueva contraseña
                    PasswordTextField(
                        label = stringResource(R.string.Cambiar_contrasena_nueva),
                        value = nuevaContrasena,
                        onValueChange = {
                            nuevaContrasena = it
                            viewModel.clearError()
                        },
                        isError = uiState.validationErrors.isNotEmpty() && nuevaContrasena.length < 8,
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    // Campo: Confirmar contraseña
                    PasswordTextField(
                        label = stringResource(R.string.Cambiar_contrasena_confirmar),
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            viewModel.clearError()
                        },
                        isError = uiState.validationErrors.isNotEmpty() && confirmarContrasena != nuevaContrasena,
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Botón CONFIRMAR
                    SecondaryButton(
                        text = if (uiState.isLoading) "Cambiando..." else stringResource(R.string.Cambiar_contrasena_confirmar_boton),
                        onClick = {
                            viewModel.changePassword(
                                contrasenaActual,
                                nuevaContrasena,
                                confirmarContrasena
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = UiConstants.PADDING_EXTRA_LARGE.dp),
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        enabled = !uiState.isLoading
                    )

                    // Mostrar errores de validación si existen
                    if (uiState.validationErrors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            uiState.validationErrors.forEach { error ->
                                Text(
                                    text = "• $error",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Mostrar error general si existe
                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = UiConstants.PADDING_LARGE.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Indicador de carga
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordScreenPreview() {
    UvgMarketTheme {
        ChangePasswordScreen()
    }
}