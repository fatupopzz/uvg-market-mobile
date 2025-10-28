package com.example.uvgmarket.chat_general.models

data class Chat(
    val id: String,
    val nombreContacto: String,
    val ultimoMensaje: String,
    val hora: String,
    val imagenPerfil: String,
    val mensajesNoLeidos: Int = 0
)