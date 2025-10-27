package com.example.uvgmarket.edit_profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.textfields.AppTextField
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun EditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = UiConstants.PADDING_SMALL.dp)
        )

        AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            textColor = MaterialTheme.colorScheme.onSurface,
            placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditTextFieldPreview() {
    UvgMarketTheme {
        EditTextField(
            label = "Nombre:",
            value = "Hamburguesas Kawaii",
            onValueChange = {},
            placeholder = "Ingresa tu nombre",
            modifier = Modifier.padding(16.dp)
        )
    }
}