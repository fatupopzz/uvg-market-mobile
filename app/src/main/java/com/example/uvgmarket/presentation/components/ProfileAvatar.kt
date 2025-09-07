package com.example.uvgmarket.presentation.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Componente reutilizable para mostrar el avatar del usuario
 *
 * @param modifier Modifier para personalizar el componente
 * @param size Tamaño del avatar en dp (default: 30dp)
 * @param onClick Callback cuando se presiona el avatar
 */
@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    size: Int = 25, // Reducido de 30 a 25 para coincidir con la imagen
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(AppColors.TextWhite),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil de usuario",
            tint = AppColors.UvgGreen,
            modifier = Modifier.size((size * 0.6).dp) // Icono más pequeño proporcionalmente
        )
    }
}

/**
 * Preview del componente ProfileAvatar
 */
@Preview(showBackground = true)
@Composable
fun ProfileAvatarPreview() {
    ProfileAvatar()
}