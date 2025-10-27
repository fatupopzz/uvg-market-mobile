package com.example.uvgmarket.profile.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun CustomEditButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Default.Edit,
            contentDescription = "Editar perfil",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun CustomEditButtonPreview() {
    UvgMarketTheme {
        CustomEditButton(onClick = {})
    }
}