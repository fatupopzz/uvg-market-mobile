package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.chat.components.Message
import com.example.uvgmarket.chat.models.Chat
import com.example.uvgmarket.chat.models.ParticipantData
import com.example.uvgmarket.data.remote.FirebaseConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val firestore: FirebaseFirestore = FirebaseConfig.getFirestore()
    private val chatsCollection = firestore.collection("chats")
    private val messagesCollection = firestore.collection("messages")
    private val TAG = "ChatRepository"

    /**
     * Obtiene o crea un chat entre dos usuarios
     */
    suspend fun getOrCreateChat(
        currentUserId: String,
        otherUserId: String,
        currentUserName: String,
        currentUserImage: String,
        otherUserName: String,
        otherUserImage: String
    ): String? {
        return try {
            Log.d(TAG, "Buscando chat entre $currentUserId y $otherUserId")

            // Buscar chat existente
            val participants = listOf(currentUserId, otherUserId).sorted()
            val existingChats = chatsCollection
                .whereArrayContains("participants", currentUserId)
                .get()
                .await()

            val existingChat = existingChats.documents.firstOrNull { doc ->
                val chatParticipants = doc.get("participants") as? List<*>
                val sortedChatParticipants = chatParticipants?.mapNotNull { it as? String }?.sorted()
                sortedChatParticipants == participants
            }

            if (existingChat != null) {
                Log.d(TAG, "Chat existente encontrado: ${existingChat.id}")
                return existingChat.id
            }

            // Crear nuevo chat
            Log.d(TAG, "Creando nuevo chat")
            val chatData = Chat(
                participants = participants,
                lastMessage = "",
                lastMessageTimestamp = System.currentTimeMillis(),
                participantsData = mapOf(
                    currentUserId to ParticipantData(
                        userId = currentUserId,
                        name = currentUserName,
                        profileImage = currentUserImage
                    ),
                    otherUserId to ParticipantData(
                        userId = otherUserId,
                        name = otherUserName,
                        profileImage = otherUserImage
                    )
                )
            )

            val docRef = chatsCollection.document()
            docRef.set(chatData.toMap()).await()

            Log.d(TAG, "Chat creado: ${docRef.id}")
            docRef.id

        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener/crear chat", e)
            null
        }
    }

    /**
     * Escucha los mensajes de un chat en tiempo real
     */
    fun listenToMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        Log.d(TAG, "Escuchando mensajes del chat: $chatId")

        val listener = messagesCollection
            .whereEqualTo("chatId", chatId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error escuchando mensajes", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.data?.let { Message.fromMap(doc.id, it) }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parseando mensaje", e)
                            null
                        }
                    }
                    Log.d(TAG, "Mensajes recibidos: ${messages.size}")
                    trySend(messages)
                }
            }

        awaitClose {
            Log.d(TAG, "Cerrando listener de mensajes")
            listener.remove()
        }
    }

    /**
     * Envía un mensaje
     */
    suspend fun sendMessage(
        chatId: String,
        senderId: String,
        receiverId: String,
        text: String,
        senderName: String,
        senderProfileImage: String
    ): Boolean {
        return try {
            Log.d(TAG, "Enviando mensaje en chat: $chatId")

            val message = Message(
                chatId = chatId,
                senderId = senderId,
                receiverId = receiverId,
                text = text,
                timestamp = System.currentTimeMillis(),
                senderName = senderName,
                senderProfileImage = senderProfileImage
            )

            // Guardar mensaje
            val messageRef = messagesCollection.document()
            messageRef.set(message.toMap()).await()

            // Actualizar último mensaje del chat
            chatsCollection.document(chatId).update(
                mapOf(
                    "lastMessage" to text,
                    "lastMessageTimestamp" to message.timestamp,
                    "lastMessageSenderId" to senderId
                )
            ).await()

            Log.d(TAG, "Mensaje enviado exitosamente")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando mensaje", e)
            false
        }
    }

    /**
     * Obtiene los chats de un usuario
     */
    suspend fun getUserChats(userId: String): List<Chat> {
        return try {
            Log.d(TAG, "Obteniendo chats del usuario: $userId")

            val snapshot = chatsCollection
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val chats = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data?.let { Chat.fromMap(doc.id, it) }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando chat", e)
                    null
                }
            }

            Log.d(TAG, "Chats obtenidos: ${chats.size}")
            chats
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo chats", e)
            emptyList()
        }
    }
}