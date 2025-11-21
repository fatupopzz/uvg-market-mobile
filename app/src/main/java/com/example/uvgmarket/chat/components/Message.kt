package com.example.uvgmarket.chat.components

/**
 * Data class que representa un mensaje en el chat
 * Preparada para integración con Firebase Firestore
 */
data class Message(
    val id: String = "", // ID único del mensaje (Firebase document ID)
    val chatId: String = "", // ID del chat al que pertenece
    val senderId: String = "", // ID del usuario que envía
    val receiverId: String = "", // ID del usuario que recibe
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val senderName: String = "", // Nombre del remitente
    val senderProfileImage: String = "" // Imagen de perfil del remitente
) {
    /**
     * Convierte el mensaje a un Map para Firebase
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "chatId" to chatId,
            "senderId" to senderId,
            "receiverId" to receiverId,
            "text" to text,
            "timestamp" to timestamp,
            "isRead" to isRead,
            "senderName" to senderName,
            "senderProfileImage" to senderProfileImage
        )
    }

    companion object {
        /**
         * Crea un mensaje desde un Map de Firebase
         */
        fun fromMap(id: String, map: Map<String, Any>): Message {
            return Message(
                id = id,
                chatId = map["chatId"] as? String ?: "",
                senderId = map["senderId"] as? String ?: "",
                receiverId = map["receiverId"] as? String ?: "",
                text = map["text"] as? String ?: "",
                timestamp = map["timestamp"] as? Long ?: System.currentTimeMillis(),
                isRead = map["isRead"] as? Boolean ?: false,
                senderName = map["senderName"] as? String ?: "",
                senderProfileImage = map["senderProfileImage"] as? String ?: ""
            )
        }
    }
}