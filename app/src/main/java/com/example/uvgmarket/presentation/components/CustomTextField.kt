package com.example.uvgmarket.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.uvgmarket.presentation.theme.AppColors


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppColors.UvgGreenMedium,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = AppColors.TextWhite,
            unfocusedTextColor = AppColors.TextWhite,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        isError = isError
    )
}