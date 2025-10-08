package com.example.uvgmarket.Ordenes_Adr.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.core.ui.components.buttons.SecondaryButton
import com.example.uvgmarket.core.ui.components.topbar.AppTopBar

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
        // Top bar
        AppTopBar(
            onBackClick = onBackClick,
            backgroundColor = Color(0xFF4A7E39)
        )

        // Product image
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

        // Product information
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8E8E8))
                .padding(
                    horizontal = UiConstants.PADDING_LARGE.dp,
                    vertical = UiConstants.PADDING_EXTRA_LARGE.dp
                ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Pandita Hamburguesa",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A5A4A),
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Gruesa y caliente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6A6A6A),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiConstants.PADDING_SMALL.dp)
            )

            Text(
                text = "Rica hamburguesa libre de gluten sin ningún tipo de preservantes.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF4A4A4A),
                textAlign = TextAlign.Left,
                lineHeight = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiConstants.PADDING_LARGE.dp)
            )

            Text(
                text = "Q39.00",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A5A4A),
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiConstants.PADDING_EXTRA_LARGE.dp)
            )

            SecondaryButton(
                text = stringResource(R.string.boton_contactar_vendedor),
                onClick = onContactSellerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                containerColor = Color(0xFF4A7E39),
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