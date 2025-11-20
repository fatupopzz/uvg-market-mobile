package com.example.uvgmarket.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.R
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.ui.theme.UvgMarketTheme

@Composable
fun ProfileProductCard(
    producto: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDeleteButton: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RectangleShape
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Imagen del producto
                    val imageResId = context.resources.getIdentifier(
                        producto.imagen,
                        "drawable",
                        context.packageName
                    )

                    Image(
                        painter = painterResource(
                            id = if (imageResId != 0) imageResId else R.drawable.product_placeholder
                        ),
                        contentDescription = producto.nombre,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Información del producto
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = producto.nombre,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = producto.descripcion,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = "Q. ${String.format("%.1f", producto.precio)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                // Botón de eliminar
                if (showDeleteButton) {
                    CustomDeleteButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    )
                }
            }

            CustomDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileProductCardPreview() {
    UvgMarketTheme {
        val producto = Product(
            id = "1",
            nombre = "Hamburguesa",
            descripcion = "Hamburguesa con queso, lechuga y tomate",
            imagen = "hamburger1",
            precio = 30.0
        )

        ProfileProductCard(
            producto = producto,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileProductCardWithDeletePreview() {
    UvgMarketTheme {
        val producto = Product(
            id = "1",
            nombre = "Hamburguesa",
            descripcion = "Hamburguesa con queso, lechuga y tomate",
            imagen = "hamburger1",
            precio = 30.0
        )

        ProfileProductCard(
            producto = producto,
            onClick = {},
            showDeleteButton = true,
            onDeleteClick = {}
        )
    }
}