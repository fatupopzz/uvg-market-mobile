package com.example.uvgmarket.Ordenes_Adr.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Pantalla de detalle de producto.
 * Muestra información completa del producto seleccionado.
 *
 * @param showContactButton Si es true, muestra el botón de contactar vendedor
 */
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit = {},
    onContactSellerClick: () -> Unit = {},
    showContactButton: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar con botón de regreso
            AppTopBar(
                onBackClick = onBackClick
            )

            // Imagen del producto
            ProductImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Información del producto
            ProductInformation(
                onContactSellerClick = onContactSellerClick,
                showContactButton = showContactButton
            )
        }
    }
}

@Composable
private fun ProductImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.hamburger1),
            contentDescription = "Pandita Hamburguesa",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ProductInformation(
    onContactSellerClick: () -> Unit,
    showContactButton: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                horizontal = UiConstants.PADDING_LARGE.dp,
                vertical = UiConstants.PADDING_EXTRA_LARGE.dp
            )
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start
    ) {
        // Título del producto
        ProductTitle(text = "Pandita Hamburguesa")

        Spacer(modifier = Modifier.height(UiConstants.PADDING_SMALL.dp))

        // Subtítulo
        ProductSubtitle(text = "Gruesa y caliente")

        Spacer(modifier = Modifier.height(UiConstants.PADDING_LARGE.dp))

        // Descripción
        ProductDescription(
            text = "Rica hamburguesa libre de gluten sin ningún tipo de preservantes."
        )

        Spacer(modifier = Modifier.height(UiConstants.PADDING_EXTRA_LARGE.dp))

        // Precio
        ProductPrice(price = "Q39.00")

        Spacer(modifier = Modifier.height(40.dp))

        // Botón de contactar vendedor
        if (showContactButton) {
            SecondaryButton(
                text = stringResource(R.string.boton_contactar_vendedor),
                onClick = onContactSellerClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProductTitle(text: String) {
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductSubtitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductDescription(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Left,
        lineHeight = 22.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductPrice(price: String) {
    Text(
        text = price,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    UvgMarketTheme {
        ProductDetailScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenWithoutContactButtonPreview() {
    UvgMarketTheme {
        ProductDetailScreen(showContactButton = false)
    }
}