package com.example.uvgmarket.profile.data

import com.example.uvgmarket.R

object DummyData {

    val mockUsuario = mapOf(
        "id" to "1",
        "nombre" to "Hamburguesas Kawaii",
        "descripcion" to "Tu lugar favorito para comer",
        "imagenPerfil" to R.drawable.profile_picture,
        "imagenPortada" to R.drawable.portada_perfil,
        "calificacion" to 3f
    )

    val mockProductos = listOf(
        mapOf(
            "id" to "1",
            "nombre" to "Osito Hamburguesa",
            "precio" to 25.0,
            "descripcion" to "Rica y bonita para ti",
            "imagen" to R.drawable.osito_hamburguesa
        ),
        mapOf(
            "id" to "2",
            "nombre" to "Pandita Hamburguesa",
            "precio" to 39.0,
            "descripcion" to "Dulce y saludable",
            "imagen" to R.drawable.pandita_hamburguesa
        ),
        mapOf(
            "id" to "3",
            "nombre" to "Gatito Hamburguesa",
            "precio" to 32.0,
            "descripcion" to "Perfecta para los amantes de gatos",
            "imagen" to R.drawable.osito_hamburguesa
        )
    )
}