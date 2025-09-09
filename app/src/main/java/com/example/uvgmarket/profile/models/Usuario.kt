package com.example.uvgmarket.profile.models

data class Usuario(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val imagenPerfil: Int,
    val imagenPortada: Int,
    val calificacion: Float = 0f
)