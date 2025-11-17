package com.example.uvgmarket.profile.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repositorio para manejar las calificaciones de usuarios
 * Guarda en memoria (para beta/entrega)
 */
object RatingRepository {

    // Mapa de userId -> rating
    private val _userRatings = MutableStateFlow<MutableMap<String, Int>>(
        mutableMapOf(
            UserRepository.CURRENT_USER_ID to 2,
            "1" to 3,
            "2" to 5,
            "3" to 4
        )
    )
    val userRatings: StateFlow<Map<String, Int>> = _userRatings.asStateFlow()

    /**
     * Obtiene la calificación de un usuario
     */
    fun getRating(userId: String): Int {
        return _userRatings.value[userId] ?: 0
    }

    /**
     * Actualiza la calificación de un usuario
     */
    fun updateRating(userId: String, newRating: Int) {
        val currentRatings = _userRatings.value.toMutableMap()
        currentRatings[userId] = newRating
        _userRatings.value = currentRatings
    }

    /**
     * Obtiene todas las calificaciones
     */
    fun getAllRatings(): Map<String, Int> {
        return _userRatings.value
    }
}