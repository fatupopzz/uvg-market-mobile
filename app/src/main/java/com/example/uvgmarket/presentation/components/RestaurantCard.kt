package com.example.uvgmarket.presentation.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvgmarket.presentation.theme.AppColors

/**
 * Data class que representa la información de un restaurante
 */
data class Restaurant(
    val name: String,
    val description: String,
    val rating: Int,
    val profileImageRes: Int,
    val foodImages: List<Int>
)

/**
 * Card component para mostrar información de un restaurante
 */
@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
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
                // Avatar del restaurante
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(AppColors.UvgGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar de ${restaurant.name}",
                        tint = AppColors.TextWhite,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }

                // Rating con estrellas
                StarRating(
                    rating = restaurant.rating,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del restaurante
            Text(
                text = restaurant.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción del restaurante
            Text(
                text = restaurant.description,
                fontSize = 14.sp,
                color = AppColors.TextGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imágenes de comida placeholder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) {
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
 * Preview del componente RestaurantCard
 */
@Preview(showBackground = true)
@Composable
fun RestaurantCardPreview() {
    val sampleRestaurant = Restaurant(
        name = "Hamburguesas kawaii",
        description = "Tu lugar fav para comer",
        rating = 3,
        profileImageRes = 0,
        foodImages = listOf()
    )

    RestaurantCard(restaurant = sampleRestaurant)
}