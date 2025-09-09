package com.example.uvgmarket.Ordenes_Adr.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.R
import com.example.uvgmarket.Ordenes_Adr.componentes.*

/**
 * Pantalla de detalles de producto que muestra la información completa
 * de un producto seleccionado, incluyendo imagen, descripción y precio.
 *
 * @param onBackClick Callback ejecutado cuando se presiona el botón de regreso
 * @param onContactSellerClick Callback ejecutado cuando se presiona el botón de contactar vendedor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit = {},
    onContactSellerClick: () -> Unit = {}
) {
    // Estados para datos del producto (usando placeholders)
    val productTitle by remember { mutableStateOf("Pandita Hamburguesa") }
    val productSubtitle by remember { mutableStateOf("Gruesa y caliente") }
    val productDescription by remember { mutableStateOf("Rica hamburguesa libre de gluten sin ningún tipo de preservantes.") }
    val productPrice by remember { mutableStateOf("39.00Q") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header con botón de regreso
            TopAppBar(
                title = {
                    Text(
                        text = "ordenar", // Placeholder como se ve en la imagen
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF7FB069) // Color verde como en la imagen
                )
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen del producto
                ProductImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Información del producto en tarjeta
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título del producto
                        Text(
                            text = productTitle,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Subtítulo
                        Text(
                            text = productSubtitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Descripción del producto
                        Text(
                            text = productDescription,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Precio
                        Text(
                            text = productPrice,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de contactar vendedor
                CustomButton(
                    text = "CONTACTAR AL VENDEDOR",
                    onClick = onContactSellerClick,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF7FB069) // Verde
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Composable que muestra la imagen del producto
 * Utiliza un placeholder similar al ProfileImage del RegistroScreen
 *
 * @param modifier Modificadores de Compose para personalizar la apariencia
 */
@Composable
private fun ProductImage(
    modifier: Modifier = Modifier
) {
    // Placeholder para la imagen del producto
    // En una implementación real, aquí cargarías la imagen desde una URL o recurso
    Box(
        modifier = modifier.background(
            color = Color(0xFFE0E0E0),
            shape = RoundedCornerShape(12.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        // Icono placeholder o imagen real
        Text(
            text = "🍔",
            fontSize = 60.sp,
            textAlign = TextAlign.Center
        )
        /* Alternativa usando Image si tienes el recurso:
        Image(
            painter = painterResource(id = R.drawable.product_placeholder),
            contentDescription = "Imagen del producto",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        */
    }
}

/**
 * Preview de la pantalla de detalles del producto
 */
@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen()
}