package com.example.uvgmarket.pantallainicio.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Card component para mostrar información de un emprendedor/vendedor
 *
 * @param onClick Callback cuando se hace click en la card, menos estrellas y productos
 * @param onStarClick Callback cuando se hace click en las estrellas
 * @param onProductImageClick Callback cuando se hace click en una imagen de producto (recibe productId)
 */
@Composable
fun EntrepreneurCard(
    entrepreneur: Entrepreneur,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onStarClick: () -> Unit = {},
    onProductImageClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val TAG = "EntrepreneurCard"

    // NUEVO: Log para depurar
    LaunchedEffect(entrepreneur.rating) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "Card renderizada para: ${entrepreneur.name}")
        Log.d(TAG, "Rating recibido: ${entrepreneur.rating}")
        Log.d(TAG, "ID del emprendedor: ${entrepreneur.id}")
        Log.d(TAG, "========================================")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            // Fila superior: Avatar + Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar del emprendedor con imagen real
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    // Obtener el resource ID dinámicamente
                    val profileImageResId = context.resources.getIdentifier(
                        entrepreneur.profileImage,
                        "drawable",
                        context.packageName
                    )

                    if (profileImageResId != 0) {
                        Image(
                            painter = painterResource(id = profileImageResId),
                            contentDescription = "Avatar de ${entrepreneur.name}",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Fallback al ícono por defecto
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar de ${entrepreneur.name}",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                // Rating con estrellas
                Box(
                    modifier = Modifier
                        .clickable { onStarClick() }
                        .padding(top = 4.dp)
                ) {
                    // NUEVO: Log antes de mostrar las estrellas
                    Log.d(TAG, "Mostrando ${entrepreneur.rating} estrellas para ${entrepreneur.name}")
                    StarRating(rating = entrepreneur.rating)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del emprendedor
            Text(
                text = entrepreneur.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción del emprendedor
            Text(
                text = entrepreneur.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Productos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                entrepreneur.productImages.forEachIndexed { index, imageName ->
                    val productId = entrepreneur.productIds.getOrNull(index) ?: imageName

                    val productImageResId = context.resources.getIdentifier(
                        imageName,
                        "drawable",
                        context.packageName
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onProductImageClick(productId)
                            }
                    ) {
                        if (productImageResId != 0) {
                            Image(
                                painter = painterResource(id = productImageResId),
                                contentDescription = "Producto ${index + 1} de ${entrepreneur.name}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                        }
                    }
                }

                // Espacios vacíos si hay menos de 2 productos
                repeat(2 - entrepreneur.productImages.size.coerceAtMost(2)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }
    }
}

/**
 * Preview del componente EntrepreneurCard
 */
@Preview(showBackground = true)
@Composable
fun EntrepreneurCardPreview() {
    UvgMarketTheme {
        val sampleEntrepreneur = Entrepreneur(
            id = "1",
            name = "Hamburguesas kawaii",
            description = "Tu lugar fav para comer",
            rating = 3,
            profileImage = "hamburger1",
            productImages = listOf("hamburger1", "hamburger2"),
            productIds = listOf("prod1", "prod2")
        )

        EntrepreneurCard(entrepreneur = sampleEntrepreneur)
    }
}