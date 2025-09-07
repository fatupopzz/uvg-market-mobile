package com.example.uvgmarket.profile.repository

import com.example.uvgmarket.profile.data.DummyData
import com.example.uvgmarket.profile.models.Producto
import com.example.uvgmarket.profile.models.Usuario

class DummyRepository {

    fun getUsuario(): Usuario {
        val data = DummyData.mockUsuario
        return Usuario(
            id = data["id"] as String,
            nombre = data["nombre"] as String,
            descripcion = data["descripcion"] as String,
            imagenPerfil = data["imagenPerfil"] as Int,
            imagenPortada = data["imagenPortada"] as Int,
            calificacion = data["calificacion"] as Float
        )
    }

    fun getProductos(): List<Producto> {
        return DummyData.mockProductos.map { data ->
            Producto(
                id = data["id"] as String,
                nombre = data["nombre"] as String,
                precio = data["precio"] as Double,
                descripcion = data["descripcion"] as String,
                imagen = data["imagen"] as Int
            )
        }
    }

    fun getProductoById(id: String): Producto? {
        return getProductos().find { it.id == id }
    }
}