package com.example.uvgmarket.chat.models

/**
 * Data class que representa una conversación de chat
 */
data class Chat(
    val id: String = "", // ID único del chat
    val participants: List<String> = emptyList(), // IDs de los participantes
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val lastMessageSenderId: String = "",
    val participantsData: Map<String, ParticipantData> = emptyMap() // Datos de cada participante
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "participants" to participants,
            "lastMessage" to lastMessage,
            "lastMessageTimestamp" to lastMessageTimestamp,
            "lastMessageSenderId" to lastMessageSenderId,
            "participantsData" to participantsData.mapValues { it.value.toMap() }
        )
    }

    companion object {
        fun fromMap(id: String, map: Map<String, Any>): Chat {
            val participantsDataMap = (map["participantsData"] as? Map<String, Map<String, Any>>)
                ?.mapValues { ParticipantData.fromMap(it.value) } ?: emptyMap()

            return Chat(
                id = id,
                participants = (map["participants"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                lastMessage = map["lastMessage"] as? String ?: "",
                lastMessageTimestamp = map["lastMessageTimestamp"] as? Long ?: System.currentTimeMillis(),
                lastMessageSenderId = map["lastMessageSenderId"] as? String ?: "",
                participantsData = participantsDataMap
            )
        }
    }
}

/**
 * Datos de un participante en el chat
 */
data class ParticipantData(
    val userId: String = "",
    val name: String = "",
    val profileImage: String = "",
    val unreadCount: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "profileImage" to profileImage,
            "unreadCount" to unreadCount
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): ParticipantData {
            return ParticipantData(
                userId = map["userId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                profileImage = map["profileImage"] as? String ?: "",
                unreadCount = (map["unreadCount"] as? Long)?.toInt() ?: 0
            )
        }
    }
}