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
import com.example.uvgmarket.R
import com.example.uvgmarket.change_password.components.PasswordChangeTopBar
import com.example.uvgmarket.change_password.components.PasswordTextField
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.constants.ValidationConstants
import com.example.uvgmarket.core.ui.components.buttons.SecondaryButton
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit = {},
    onConfirmClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var contrasenaActual by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val validationErrors = remember(
        contrasenaActual, nuevaContrasena, confirmarContrasena, showErrors
    ) {
        if (showErrors) {
            getPasswordValidationErrors(contrasenaActual, nuevaContrasena, confirmarContrasena)
        } else {
            emptyList()
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
                            showErrors = false
                        },
                        isError = showErrors && contrasenaActual.isBlank()
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    // Campo: Nueva contraseña
                    PasswordTextField(
                        label = stringResource(R.string.Cambiar_contrasena_nueva),
                        value = nuevaContrasena,
                        onValueChange = {
                            nuevaContrasena = it
                            showErrors = false
                        },
                        isError = showErrors && nuevaContrasena.length < ValidationConstants.MIN_PASSWORD_LENGTH
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    // Campo: Confirmar contraseña
                    PasswordTextField(
                        label = stringResource(R.string.Cambiar_contrasena_confirmar),
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            showErrors = false
                        },
                        isError = showErrors && confirmarContrasena != nuevaContrasena
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Botón CONFIRMAR
                    SecondaryButton(
                        text = stringResource(R.string.Cambiar_contrasena_confirmar_boton),
                        onClick = {
                            if (isValidPasswordChange(contrasenaActual, nuevaContrasena, confirmarContrasena)) {
                                onConfirmClick(contrasenaActual, nuevaContrasena, confirmarContrasena)
                            } else {
                                showErrors = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = UiConstants.PADDING_EXTRA_LARGE.dp),
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )

                    // Mostrar errores de validación si existen
                    if (validationErrors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            validationErrors.forEach { error ->
                                Text(
                                    text = "• $error",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

private fun isValidPasswordChange(
    contrasenaActual: String,
    nuevaContrasena: String,
    confirmarContrasena: String
): Boolean {
    return contrasenaActual.isNotBlank() &&
            nuevaContrasena.length >= ValidationConstants.MIN_PASSWORD_LENGTH &&
            nuevaContrasena == confirmarContrasena &&
            contrasenaActual != nuevaContrasena
}

private fun getPasswordValidationErrors(
    contrasenaActual: String,
    nuevaContrasena: String,
    confirmarContrasena: String
): List<String> {
    val errors = mutableListOf<String>()

    if (contrasenaActual.isBlank()) {
        errors.add("Debes ingresar tu contraseña actual")
    }

    if (nuevaContrasena.length < ValidationConstants.MIN_PASSWORD_LENGTH) {
        errors.add("La nueva contraseña debe tener al menos ${ValidationConstants.MIN_PASSWORD_LENGTH} caracteres")
    }

    if (nuevaContrasena != confirmarContrasena) {
        errors.add("Las contraseñas no coinciden")
    }

    if (contrasenaActual.isNotBlank() && contrasenaActual == nuevaContrasena) {
        errors.add("La nueva contraseña debe ser diferente a la actual")
    }

    return errors
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangePasswordScreenPreview() {
    UvgMarketTheme {
        ChangePasswordScreen()
    }
}