package com.example.uvgmarket.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Card component para mostrar información de un emprendedor/vendedor
 */
@Composable
fun EntrepreneurCard(
    entrepreneur: Entrepreneur,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                        .background(AppColors.UvgGreen)
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
                            tint = AppColors.TextWhite,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                // Rating con estrellas
                StarRating(
                    rating = entrepreneur.rating,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del emprendedor
            Text(
                text = entrepreneur.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción del emprendedor
            Text(
                text = entrepreneur.description,
                fontSize = 14.sp,
                color = AppColors.TextGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imágenes de productos reales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mostrar hasta 2 imágenes de productos
                entrepreneur.productImages.take(2).forEachIndexed { index, imageName ->
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
                            // Fallback a caja gris si no se encuentra la imagen
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(AppColors.SurfaceGray)
                            )
                        }
                    }
                }

                repeat(2 - entrepreneur.productImages.size.coerceAtMost(2)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppColors.SurfaceGray)
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
    val sampleEntrepreneur = Entrepreneur(
        name = "Hamburguesas kawaii",
        description = "Tu lugar fav para comer",
        rating = 3,
        profileImage = "fotoperfilcomida",
        productImages = listOf("producto1comida", "producto2comida")
    )

    EntrepreneurCard(entrepreneur = sampleEntrepreneur)
}