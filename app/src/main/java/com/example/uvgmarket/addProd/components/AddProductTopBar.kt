package com.example.uvgmarket.addProd.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.R
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Barra superior con botones Cancelar y Publicar para agregar productos
 * Utiliza los colores del tema para mantener consistencia con ProfileScreen
 *
 * @param onCancelar Callback cuando se presiona el botón Cancelar
 * @param onPublicar Callback cuando se presiona el botón Publicar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductTopBar(
    onCancelar: () -> Unit,
    onPublicar: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            TextButton(onClick = onCancelar) {
                Text(
                    text = stringResource(R.string.cancelar),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }
        },
        actions = {
            TextButton(onClick = onPublicar) {
                Text(
                    text = stringResource(R.string.publicar),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Preview
@Composable
fun AddProductTopBarPreview() {
    UvgMarketTheme {
        AddProductTopBar(
            onCancelar = {},
            onPublicar = {}
        )
    }
}