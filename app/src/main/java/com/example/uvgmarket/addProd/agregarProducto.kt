package com.example.uvgmarket.addProd

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.uvgmarket.R
import com.example.uvgmarket.ui.theme.*

/**
 * Pantalla para agregar un nuevo producto al marketplace
 *
 * Esta pantalla permite al usuario:
 * - Adjuntar una imagen del producto
 * - Ingresar el nombre del producto
 * - Agregar una descripción
 * - Especificar el precio
 *
 * @param onCancelar Callback que se ejecuta cuando el usuario presiona "Cancelar"
 * @param onPublicar Callback que se ejecuta cuando el usuario presiona "Publicar"
 *                   Recibe como parámetros: nombre, descripción, precio e imagen URI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(
    onCancelar: () -> Unit = {},
    onPublicar: (String, String, String, Uri?) -> Unit = { _, _, _, _ -> }
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = onCancelar) {
                        Text(
                            text = stringResource(R.string.cancelar),
                            color = TextWhite,
                            fontSize = 16.sp
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        onPublicar(nombre, descripcion, precio, imageUri)
                    }) {
                        Text(
                            text = stringResource(R.string.publicar),
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = UvgGreen
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección de imagen
            ImagePickerSection(
                imageUri = imageUri,
                onImageClick = { imagePickerLauncher.launch("image/*") }
            )

            Divider(
                color = SurfaceGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Formulario de producto
            ProductForm(
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

/**
 * Componente que muestra la sección para seleccionar/mostrar la imagen del producto
 *
 * @param imageUri URI de la imagen seleccionada (null si no hay imagen)
 * @param onImageClick Callback que se ejecuta cuando se hace clic en el área de la imagen
 */
@Composable
private fun ImagePickerSection(
    imageUri: Uri?,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Círculo con el icono de agregar imagen o la imagen seleccionada
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(3.dp, UvgGreenMedium, CircleShape)
                .background(SurfaceWhite)
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                // Mostrar la imagen seleccionada
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = stringResource(R.string.adjuntar_imagen),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Mostrar el icono de agregar
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.adjuntar_imagen),
                    tint = UvgGreenMedium,
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto "Adjuntar una Imagen"
        Text(
            text = stringResource(R.string.adjuntar_imagen),
            fontSize = 16.sp,
            color = TextDark,
            fontWeight = FontWeight.Normal
        )
    }
}

/**
 * Formulario con los campos de entrada para los datos del producto
 *
 * @param nombre Valor actual del campo nombre
 * @param onNombreChange Callback cuando cambia el nombre
 * @param descripcion Valor actual del campo descripción
 * @param onDescripcionChange Callback cuando cambia la descripción
 * @param precio Valor actual del campo precio
 * @param onPrecioChange Callback cuando cambia el precio
 */
@Composable
private fun ProductForm(
    nombre: String,
    onNombreChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    precio: String,
    onPrecioChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Campo Nombre
        ProductTextField(
            label = stringResource(R.string.nombre),
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = stringResource(R.string.hint_nombre),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo Descripción
        ProductTextField(
            label = stringResource(R.string.descripcion),
            value = descripcion,
            onValueChange = onDescripcionChange,
            placeholder = stringResource(R.string.hint_descripcion),
            singleLine = false,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo Precio
        ProductTextField(
            label = stringResource(R.string.precio),
            value = precio,
            onValueChange = onPrecioChange,
            placeholder = stringResource(R.string.hint_precio),
            singleLine = true
        )
    }
}

/**
 * Campo de texto personalizado para el formulario de producto
 *
 * @param label Etiqueta del campo
 * @param value Valor actual del campo
 * @param onValueChange Callback cuando cambia el valor
 * @param placeholder Texto de ayuda cuando el campo está vacío
 * @param singleLine Si el campo es de una sola línea
 * @param minLines Número mínimo de líneas para campos multilínea
 */
@Composable
private fun ProductTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            color = TextDark,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!singleLine) Modifier.height(100.dp)
                    else Modifier
                ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextGray,
                    fontSize = 14.sp
                )
            },
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = SurfaceGray,
                focusedBorderColor = UvgGreen,
                unfocusedContainerColor = SurfaceGray,
                focusedContainerColor = SurfaceGray,
                focusedTextColor = TextDark,
                unfocusedTextColor = TextDark
            )
        )
    }
}

// ============================================
// PREVIEWS
// ============================================

/**
 * Preview de la pantalla vacía (estado inicial)
 */
@Preview(
    name = "Pantalla Vacía",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AgregarProductoScreenPreview() {
    UvgMarketTheme {
        AgregarProductoScreen(
            onCancelar = { },
            onPublicar = { _, _, _, _ -> }
        )
    }
}

/**
 * Preview de la pantalla con datos de ejemplo
 */
@Preview(
    name = "Pantalla con Datos",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AgregarProductoScreenFilledPreview() {
    UvgMarketTheme {
        AgregarProductoScreenContent(
            nombre = "Laptop Dell XPS 15",
            descripcion = "Laptop en excelente estado, poco uso. Incluye cargador original y funda protectora.",
            precio = "Q8,500",
            imageUri = null
        )
    }
}

/**
 * Preview del componente de imagen sin foto
 */
@Preview(
    name = "Selector de Imagen",
    showBackground = true
)
@Composable
fun ImagePickerSectionPreview() {
    UvgMarketTheme {
        ImagePickerSection(
            imageUri = null,
            onImageClick = { }
        )
    }
}

/**
 * Preview del formulario con datos
 */
@Preview(
    name = "Formulario con Datos",
    showBackground = true
)
@Composable
fun ProductFormPreview() {
    UvgMarketTheme {
        ProductForm(
            nombre = "iPhone 13 Pro",
            onNombreChange = { },
            descripcion = "128GB, color azul, con caja original",
            onDescripcionChange = { },
            precio = "Q6,000",
            onPrecioChange = { }
        )
    }
}

/**
 * Versión de contenido de la pantalla para preview con datos
 * (sin launcher de imagen para que funcione en preview)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgregarProductoScreenContent(
    nombre: String,
    descripcion: String,
    precio: String,
    imageUri: Uri?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = { }) {
                        Text(
                            text = "Cancelar",
                            color = TextWhite,
                            fontSize = 16.sp
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { }) {
                        Text(
                            text = "Publicar",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = UvgGreen
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagePickerSection(
                imageUri = imageUri,
                onImageClick = { }
            )

            Divider(
                color = SurfaceGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            ProductForm(
                nombre = nombre,
                onNombreChange = { },
                descripcion = descripcion,
                onDescripcionChange = { },
                precio = precio,
                onPrecioChange = { }
            )
        }
    }
}