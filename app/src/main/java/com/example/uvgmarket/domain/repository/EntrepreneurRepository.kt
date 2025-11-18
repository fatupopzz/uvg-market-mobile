package com.example.uvgmarket.domain.repository

import com.example.uvgmarket.domain.model.Entrepreneur

/**
 * Interfaz del repositorio de emprendedores
 */
interface EntrepreneurRepository {

    suspend fun getAllEntrepreneurs(): Result<List<Entrepreneur>>

    suspend fun getEntrepreneurById(entrepreneurId: String): Result<Entrepreneur>

    suspend fun searchEntrepreneurs(query: String): Result<List<Entrepreneur>>

    suspend fun updateRating(entrepreneurId: String, newRating: Int): Result<Unit>

    suspend fun getEntrepreneursByCategory(categoryId: String): Result<List<Entrepreneur>>

    suspend fun getVerifiedEntrepreneurs(): Result<List<Entrepreneur>>
}
