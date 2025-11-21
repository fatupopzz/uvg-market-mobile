package com.example.uvgmarket.change_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    var showSuccessDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Manejar el éxito del cambio de contraseña
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            showSuccessDialog = true
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡Contraseña Cambiada!",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    text = "Tu contraseña ha sido actualizada exitosamente.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.resetState()
                            onPasswordChanged()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Continuar")
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
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
            item {
                PasswordChangeTopBar(onBackClick = onBackClick)
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UiConstants.PADDING_LARGE.dp)
                        .padding(top = UiConstants.PADDING_EXTRA_LARGE.dp)
                ) {
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

                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = UiConstants.PADDING_LARGE.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

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