package com.example.uvgmarket.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Componente reutilizable para mostrar el avatar del usuario
 */
@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    size: Int = 25,
    profileImage: String? = null, // Nombre del archivo de imagen del usuario principal
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(AppColors.TextWhite),
        contentAlignment = Alignment.Center
    ) {
        if (profileImage != null) {
            // Obtener el resource ID dinámicamente
            val imageResId = context.resources.getIdentifier(
                profileImage, // Removido .lowercase() aquí
                "drawable",
                context.packageName
            )

            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(size.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback al ícono por defecto si no se encuentra la imagen
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil de usuario",
                    tint = AppColors.UvgGreen,
                    modifier = Modifier.size((size * 0.6).dp)
                )
            }
        } else {
            // Ícono por defecto cuando no hay imagen
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil de usuario",
                tint = AppColors.UvgGreen,
                modifier = Modifier.size((size * 0.6).dp)
            )
        }
    }
}

/**
 * Preview del componente ProfileAvatar
 */
@Preview(showBackground = true)
@Composable
fun ProfileAvatarPreview() {
    ProfileAvatar(profileImage = "fotodeperfilindu")
}