package com.example.uvgmarket.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.R
import com.example.uvgmarket.profile.theme.AppColors

@Composable
fun CustomChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.chat),
            contentDescription = "Chat",
            tint = AppColors.UvgGreen
        )
    }
}