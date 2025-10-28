package com.example.uvgmarket.pantallainicio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Barra de búsqueda personalizada con ícono de menú y búsqueda
 * Sigue el diseño del mockup con esquinas redondeadas
 *
 * @param searchText Texto actual de búsqueda
 * @param onSearchTextChange Callback cuando cambia el texto de búsqueda
 * @param placeholder Texto de placeholder a mostrar
 * @param onSearchClick Callback cuando se presiona el ícono de búsqueda
 */
@Composable
fun CustomSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Hinted search text",
    onSearchClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 3.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Campo de texto de búsqueda
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 11.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Placeholder cuando no hay texto
            if (searchText.isEmpty()) {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 11.sp
                )
            }
        }

        // Ícono de búsqueda
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(12.dp)
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