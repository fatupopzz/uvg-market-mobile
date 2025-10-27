package com.example.uvgmarket.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun EditProfileTopBar(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(UiConstants.TOP_BAR_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = UiConstants.PADDING_SMALL.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón Cancelar
        TextButton(onClick = onCancelClick) {
            Text(
                text = "Cancelar",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Botón Guardar
        TextButton(onClick = onSaveClick) {
            Text(
                text = "Guardar",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun EditProfileTopBarPreview() {
    UvgMarketTheme {
        EditProfileTopBar(
            onCancelClick = {},
            onSaveClick = {}
        )
    }
}