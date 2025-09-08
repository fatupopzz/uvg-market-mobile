package com.example.uvgmarket.profile.repository

import com.example.uvgmarket.profile.data.DummyData
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto

class DummyRepository {
    fun getUsuario(): Usuario = DummyData.mockUsuario

    fun getProductos(): List<Producto> = DummyData.mockProductos
}