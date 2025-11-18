package com.example.uvgmarket.domain.model

/**
 * Modelo de dominio para Chat
 * Representa una conversación en la lista de chats
 */
data class Chat(
    val id: String,
    val nombreContacto: String,
    val ultimoMensaje: String,
    val hora: String,
    val imagenPerfil: String,
    val mensajesNoLeidos: Int = 0,
    val contactoId: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Verifica si hay mensajes sin leer
     */
    fun hasUnreadMessages(): Boolean = mensajesNoLeidos > 0

    /**
     * Obtiene el badge de mensajes no leídos como string
     */
    fun getUnreadBadge(): String = if (mensajesNoLeidos > 99) "99+" else mensajesNoLeidos.toString()
}
