package com.example.uvgmarket.addProd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uvgmarket.addProd.components.*
import com.example.uvgmarket.ui.theme.*

/**
 * Pantalla para agregar un nuevo producto al marketplace
 *
 * @param onCancelar Callback cuando se presiona Cancelar
 * @param onPublicar Callback cuando se presiona Publicar con los datos del producto
 */
@Composable
fun AgregarProductoScreen(
    onCancelar: () -> Unit = {},
    onPublicar: (nombre: String, descripcion: String, precio: String, tieneImagen: Boolean) -> Unit = { _, _, _, _ -> }
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenSeleccionada by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AddProductTopBar(
                onCancelar = onCancelar,
                onPublicar = { onPublicar(nombre, descripcion, precio, imagenSeleccionada) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageSection(
                imagenSeleccionada = imagenSeleccionada,
                onImagenClick = { imagenSeleccionada = !imagenSeleccionada }
            )

            CustomDivider()

            FormSection(
                nombre = nombre,
                onNombreChange = { nombre = it },
                descripcion = descripcion,
                onDescripcionChange = { descripcion = it },
                precio = precio,
                onPrecioChange = { precio = it }
            )
        }
    }
}

// ============================================
// PREVIEWS
// ============================================

/**
 * Preview de la pantalla vacía
 */
@Preview(
    name = "Pantalla Vacía",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AgregarProductoScreenPreview() {
    UvgMarketTheme {
        AgregarProductoScreen()
    }
}

/**
 * Preview con datos de ejemplo
 */
@Preview(
    name = "Pantalla con Datos",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AgregarProductoScreenFilledPreview() {
    UvgMarketTheme {
        AgregarProductoScreenWithData()
    }
}

/**
 * Preview de la sección de imagen
 */
@Preview(
    name = "Selector de Imagen",
    showBackground = true
)
@Composable
fun ImageSectionPreview() {
    UvgMarketTheme {
        ImageSection(
            imagenSeleccionada = false,
            onImagenClick = { }
        )
    }
}

/**
 * Preview de la sección de imagen seleccionada
 */
@Preview(
    name = "Imagen Seleccionada",
    showBackground = true
)
@Composable
fun ImageSectionSelectedPreview() {
    UvgMarketTheme {
        ImageSection(
            imagenSeleccionada = true,
            onImagenClick = { }
        )
    }
}

/**
 * Versión con datos para preview
 */
@Composable
private fun AgregarProductoScreenWithData() {
    var nombre by remember { mutableStateOf("Laptop Dell XPS 15") }
    var descripcion by remember { mutableStateOf("Laptop en excelente estado, poco uso. Incluye cargador original y funda protectora.") }
    var precio by remember { mutableStateOf("Q8,500") }
    var imagenSeleccionada by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            AddProductTopBar(
                onCancelar = { },
                onPublicar = { }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageSection(
                imagenSeleccionada = imagenSeleccionada,
                onImagenClick = { imagenSeleccionada = !imagenSeleccionada }
            )

            CustomDivider()

            FormSection(
                nombre = nombre,
                onNombreChange = { nombre = it },
                descripcion = descripcion,
                onDescripcionChange = { descripcion = it },
                precio = precio,
                onPrecioChange = { precio = it }
            )
        }
    }
}