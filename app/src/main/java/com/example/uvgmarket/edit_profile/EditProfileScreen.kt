package com.example.uvgmarket.edit_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.R
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.edit_profile.components.ChangePasswordButton
import com.example.uvgmarket.edit_profile.components.EditCoverImage
import com.example.uvgmarket.edit_profile.components.EditProfileAvatar
import com.example.uvgmarket.edit_profile.components.EditProfileTopBar
import com.example.uvgmarket.edit_profile.components.EditTextField
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun EditProfileScreen(
    nombre: String = "",
    usuario: String = "",
    correo: String = "",
    imagenPerfil: Int = R.drawable.profile_picture,
    imagenPortada: Int = R.drawable.portada_perfil,
    onCancelClick: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: EditProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSaveSuccess()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            item {
                EditProfileTopBar(
                    onCancelClick = onCancelClick,
                    onSaveClick = {
                        viewModel.saveProfile()
                    }
                )
            }

            // Cover Image con botón de cámara
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    EditCoverImage(
                        imageRes = imagenPortada,
                        onCameraClick = { viewModel.onCoverImageChange() }
                    )
                }
            }

            // NUEVO: Avatar dentro del LazyColumn con offset negativo
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-70).dp)
                        .padding(start = 16.dp)
                ) {
                    EditProfileAvatar(
                        imageRes = imagenPerfil,
                        onCameraClick = { viewModel.onProfileImageChange() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }

            // Formulario de edición (con offset negativo para compensar)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-70).dp)
                        .padding(horizontal = UiConstants.PADDING_LARGE.dp)
                ) {
                    EditTextField(
                        label = stringResource(R.string.Nombre_editar_perfil),
                        value = uiState.nombre,
                        onValueChange = { viewModel.updateNombre(it) },
                        placeholder = "Ingresa tu nombre"
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    EditTextField(
                        label = stringResource(R.string.Usuario_editar_perfil),
                        value = uiState.usuario,
                        onValueChange = { viewModel.updateUsuario(it) },
                        placeholder = "Ingresa tu usuario"
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    EditTextField(
                        label = stringResource(R.string.Correo_editar_perfil),
                        value = uiState.correo,
                        onValueChange = { viewModel.updateCorreo(it) },
                        placeholder = "Ingresa tu correo"
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_EXTRA_LARGE.dp))

                    ChangePasswordButton(
                        text = stringResource(R.string.Cambiar_contrasena),
                        onClick = onChangePassword,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    LogoutButton(
                        onClick = onLogout,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

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

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun LogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = UiConstants.PADDING_EXTRA_LARGE.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD32F2F),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = "CERRAR SESIÓN",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    UvgMarketTheme {
        EditProfileScreen()
    }
}