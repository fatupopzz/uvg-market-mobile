package com.example.uvgmarket.addProd.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Divisor horizontal personalizado
 *
 * Usa el color surfaceVariant del tema para mantener consistencia visual
 */
@Composable
fun CustomDivider() {
    Divider(
        color = MaterialTheme.colorScheme.surfaceVariant,
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}