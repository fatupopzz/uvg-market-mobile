package com.example.uvgmarket.pantallainicio.components

/**
 * Data class que representa la información de un emprendedor/vendedor
 */
data class Entrepreneur(
    val name: String,
    val description: String,
    val rating: Int,
    val profileImage: String,
    val productImages: List<String>
)