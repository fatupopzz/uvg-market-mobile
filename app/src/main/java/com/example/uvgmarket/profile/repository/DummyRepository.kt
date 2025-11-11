package com.example.uvgmarket.profile.repository

import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto

class DummyRepository {
    fun getUsuario(): Usuario = UserRepository.getCurrentUser()

    fun getProductos(): List<Producto> = UserRepository.getCurrentUserProducts()
}