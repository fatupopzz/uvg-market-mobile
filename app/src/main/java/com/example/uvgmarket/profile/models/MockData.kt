package com.example.uvgmarket.profile.models

import com.example.uvgmarket.R

object MockData {

    fun getMockUsuario(): Usuario = Usuario(
        id = "1",
        nombre = "Hamburguesas Kawaii",
        descripcion = "Tu lugar favorito para comer",
        imagenPerfil = R.drawable.profile_picture,
        imagenPortada = R.drawable.portada_perfil,
        calificacion = 3f
    )

    fun getMockProductos(): List<Producto> = listOf(
        Producto(
            id = "1",
            nombre = "Osito Hamburguesa",
            precio = 25.0,
            descripcion = "Rica y bonita para ti",
            imagen = R.drawable.osito_hamburguesa
        ),
        Producto(
            id = "2",
            nombre = "Pandita Hamburguesa",
            precio = 39.0,
            descripcion = "Dulce y saludable",
            imagen = R.drawable.pandita_hamburguesa
        ),
        Producto(
            id = "3",
            nombre = "Gatito Hamburguesa",
            precio = 32.0,
            descripcion = "Perfecta para los amantes de gatos",
            imagen = R.drawable.osito_hamburguesa
        )
    )
}