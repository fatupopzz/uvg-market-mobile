package com.example.uvgmarket.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Objeto que contiene todos los colores de la aplicación UVG Market
 * Basado en la paleta de colores institucional de la UVG
 */
object AppColors {

    // Colores principales UVG del Figma
    val UvgGreen = Color(0xFF355E34)
    val UvgGreenDark = Color(0xB7152814)
    val UvgGreenMedium = Color(0xFF264425) // Verde medio oscuro
    val UvgGreenLight = Color(0xFF99B697) // Verde claro

    // Colores de fondo y superficie
    val BackgroundWhite = Color(0xFFFEFEFE) // Blanco para fondo
    val CardBackground = Color(0xFFFFFFFF) // Fondo de cards
    val SurfaceGray = Color(0xFFF5F5F5) // Gris claro para superficies

    // Colores de texto
    val TextWhite = Color(0xFFFFFFFF) // Texto blanco
    val TextDark = Color(0xFF264425) // Texto oscuro
    val TextGray = Color(0xFF757575) // Texto secundario
    val TextHint = Color(0xFF9E9E9E) // Texto de hint

    // Colores de botones y elementos interactivos
    val ButtonWhite = Color(0xFFFFFFFF) // Botón blanco
    val IconGray = Color(0xFF616161) // Iconos grises

    // Colores de rating
    val StarActive = UvgGreen // Estrella activa
    val StarInactive = Color(0xFFE0E0E0) // Estrella inactiva
}