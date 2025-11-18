package com.example.uvgmarket.domain.model

/**
 * Modelo de dominio para Emprendedor
 * Representa un vendedor en el marketplace
 */
data class Entrepreneur(
    val id: String,
    val name: String,
    val description: String,
    val rating: Int,
    val profileImage: String,
    val productImages: List<String>,
    val coverImage: String = "",
    val totalSales: Int = 0,
    val isVerified: Boolean = false
) {
    /**
     * Valida si el rating está en el rango correcto
     */
    fun isValidRating(): Boolean = rating in 0..5

    /**
     * Verifica si tiene productos disponibles
     */
    fun hasProducts(): Boolean = productImages.isNotEmpty()
}
