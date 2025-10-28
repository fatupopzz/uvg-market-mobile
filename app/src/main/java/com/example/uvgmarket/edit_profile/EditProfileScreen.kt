package com.example.uvgmarket.edit_profile

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
    onSaveClick: (String, String, String) -> Unit = { _, _, _ -> },
    onChangeProfileImage: () -> Unit = {},
    onChangeCoverImage: () -> Unit = {},
    onChangePassword: () -> Unit = {}
) {
    var nombreState by remember { mutableStateOf(nombre) }
    var usuarioState by remember { mutableStateOf(usuario) }
    var correoState by remember { mutableStateOf(correo) }

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
                        onSaveClick(nombreState, usuarioState, correoState)
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
                        onCameraClick = onChangeCoverImage
                    )
                }
            }

            // Spacer para el avatar
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }

            // Formulario de edición
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UiConstants.PADDING_LARGE.dp)
                ) {
                    EditTextField(
                        label = stringResource(R.string.Nombre_editar_perfil),
                        value = nombreState,
                        onValueChange = { nombreState = it },
                        placeholder = "Ingresa tu nombre"
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    EditTextField(
                        label = stringResource(R.string.Usuario_editar_perfil),
                        value = usuarioState,
                        onValueChange = { usuarioState = it },
                        placeholder = "Ingresa tu usuario"
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

                    EditTextField(
                        label = stringResource(R.string.Correo_editar_perfil),
                        value = correoState,
                        onValueChange = { correoState = it },
                        placeholder = "Ingresa tu correo"
                    )

                    Spacer(modifier = Modifier.height(UiConstants.PADDING_EXTRA_LARGE.dp))

                    ChangePasswordButton(
                        text = stringResource(R.string.Cambiar_contrasena),
                        onClick = onChangePassword,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Avatar superpuesto
        EditProfileAvatar(
            imageRes = imagenPerfil,
            onCameraClick = onChangeProfileImage,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 16.dp, y = 170.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    UvgMarketTheme {
        EditProfileScreen(
            nombre = "Hamburguesas Kawaii",
            usuario = "hamburguesaskawaii",
            correo = "hamburguesas@uvg.edu.gt"
        )
    }
}