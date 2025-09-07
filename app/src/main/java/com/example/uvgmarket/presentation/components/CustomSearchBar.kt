package com.example.uvgmarket.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Barra de búsqueda personalizada con ícono de menú y búsqueda
 * Sigue el diseño del mockup con esquinas redondeadas
 *
 * @param searchText Texto actual de búsqueda
 * @param onSearchTextChange Callback cuando cambia el texto de búsqueda
 * @param placeholder Texto de placeholder a mostrar
 * @param onMenuClick Callback cuando se presiona el ícono de menú
 * @param onSearchClick Callback cuando se presiona el ícono de búsqueda
 */
@Composable
fun CustomSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Hinted search text",
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppColors.BackgroundWhite,
                shape = RoundedCornerShape(20.dp) // Reducido de 25dp a 20dp para que sea menos gordo
            )
            .padding(horizontal = 3.dp, vertical = 2.dp), // Reducido padding vertical
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono de menú (hamburger menu)
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(32.dp) // Reducido de 40dp a 32dp
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Abrir menú",
                tint = AppColors.IconGray,
                modifier = Modifier.size(18.dp) // Reducido de 20dp a 18dp
            )
        }

        // Campo de texto de búsqueda
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp, vertical = 2.dp) // Reducido padding
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                textStyle = TextStyle(
                    color = AppColors.TextDark,
                    fontSize = 11.sp // Reducido de 12sp a 11sp
                ),
                cursorBrush = SolidColor(AppColors.UvgGreen),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Placeholder cuando no hay texto
            if (searchText.isEmpty()) {
                Text(
                    text = placeholder,
                    color = AppColors.TextHint,
                    fontSize = 11.sp // Reducido de 12sp a 11sp
                )
            }
        }

        // Ícono de búsqueda
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(24.dp) // Reducido de 28dp a 24dp
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = AppColors.IconGray,
                modifier = Modifier.size(12.dp) // Reducido de 14dp a 12dp
            )
        }
    }
}

/**
 * Preview del componente CustomSearchBar
 */
@Preview(showBackground = true)
@Composable
fun CustomSearchBarPreview() {
    var searchText by remember { mutableStateOf("") }

    CustomSearchBar(
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        modifier = Modifier.padding(16.dp)
    )
}