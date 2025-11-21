package com.example.uvgmarket.addProd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.addProd.components.*
import com.example.uvgmarket.ui.theme.*

/**
 * Pantalla para agregar un nuevo producto al marketplace
 *
 * @param viewModel ViewModel que maneja el estado y lógica
 * @param onCancelar Callback cuando se presiona Cancelar
 * @param onPublicar Callback cuando se presiona Publicar con los datos del producto
 */
@Composable
fun AgregarProductoScreen(
    viewModel: AgregarProductoViewModel = viewModel(),
    onCancelar: () -> Unit = {},
    onPublicar: (nombre: String, descripcion: String, precio: String, tieneImagen: Boolean) -> Unit = { _, _, _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AddProductTopBar(
                onCancelar = onCancelar,
                onPublicar = {
                    if (viewModel.puedePublicar()) {
                        viewModel.publicarProducto(
                            onSuccess = {
                                // Llamar al callback de navegación
                                onPublicar(
                                    uiState.nombre,
                                    uiState.descripcion,
                                    uiState.precio,
                                    uiState.imagenSeleccionada
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageSection(
                    imagenSeleccionada = uiState.imagenSeleccionada,
                    onImagenClick = { viewModel.onImagenClick() }
                )

                CustomDivider()

                FormSection(
                    nombre = uiState.nombre,
                    onNombreChange = { viewModel.onNombreChange(it) },
                    descripcion = uiState.descripcion,
                    onDescripcionChange = { viewModel.onDescripcionChange(it) },
                    precio = uiState.precio,
                    onPrecioChange = { viewModel.onPrecioChange(it) }
                )

                // Mostrar error si existe
                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }

            // Indicador de carga
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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