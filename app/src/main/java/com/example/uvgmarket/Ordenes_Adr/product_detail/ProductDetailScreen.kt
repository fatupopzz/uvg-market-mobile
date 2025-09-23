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

@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit = {},
    onContactSellerClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header verde con flecha
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF4A7E39))
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Imagen de la hamburguesa (ocupa toda la parte superior)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.hamburger1),
                contentDescription = "Pandita Hamburguesa",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
        }

        // Sección de información (fondo gris claro)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8E8E8))
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Título
            Text(
                text = "Pandita Hamburguesa",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A5A4A),
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )

            // Subtítulo
            Text(
                text = "Gruesa y caliente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6A6A6A),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            // Descripción
            Text(
                text = "Rica hamburguesa libre de gluten sin ningún tipo de preservantes.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A4A4A),
                textAlign = TextAlign.Left,
                lineHeight = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )

            // Precio
            Text(
                text = "Q39.00",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A5A4A),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )

            // Botón
            CustomButton(
                text = stringResource(R.string.boton_contactar_vendedor),
                onClick = onContactSellerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                backgroundColor = Color(0xFF4A7E39),
                contentColor = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen()
}