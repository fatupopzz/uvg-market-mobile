package com.example.uvgmarket.core.ui.components.rating

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uvgmarket.core.constants.UiConstants
import com.example.uvgmarket.ui.theme.UvgMarketTheme

/**
 * Dialog para que el usuario califique con estrellas
 *
 * @param userName Nombre del usuario/emprendedor a calificar
 * @param currentRating Calificación actual (0-5)
 * @param onDismiss Callback cuando se cierra el dialog
 * @param onRatingSubmit Callback cuando se confirma la calificación
 */
@Composable
fun RatingDialog(
    userName: String,
    currentRating: Int,
    onDismiss: () -> Unit,
    onRatingSubmit: (Int) -> Unit
) {
    var selectedRating by remember { mutableStateOf(currentRating) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "Calificar a",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Nombre del usuario
                Text(
                    text = userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Estrellas interactivas
                InteractiveStarRating(
                    rating = selectedRating,
                    onRatingChanged = { selectedRating = it },
                    starSize = 48.dp
                )

                // Texto descriptivo
                Text(
                    text = when (selectedRating) {
                        0 -> "Sin calificación"
                        1 -> "Muy malo"
                        2 -> "Malo"
                        3 -> "Regular"
                        4 -> "Bueno"
                        5 -> "Excelente"
                        else -> ""
                    },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Cancelar
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Cancelar")
                    }

                    // Botón Calificar
                    Button(
                        onClick = {
                            onRatingSubmit(selectedRating)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedRating > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Calificar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingDialogPreview() {
    UvgMarketTheme {
        RatingDialog(
            userName = "Hamburguesas Kawaii",
            currentRating = 3,
            onDismiss = {},
            onRatingSubmit = {}
        )
    }
}