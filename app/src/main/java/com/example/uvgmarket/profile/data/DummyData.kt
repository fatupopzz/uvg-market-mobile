package com.example.uvgmarket.profile.data

import com.example.uvgmarket.R
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto

object DummyData {

    val mockUsuario = Usuario(
        id = "1",
        nombre = "Hamburguesas Kawaii",
        descripcion = "Tu lugar favorito para comer",
        imagenPerfil = R.drawable.profile_picture,
        imagenPortada = R.drawable.portada_perfil,
        calificacion = 3f
    )

    val mockProductos = listOf(
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
        )
    )
}