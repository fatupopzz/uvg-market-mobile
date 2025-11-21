package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.chat.components.Message
import com.example.uvgmarket.chat.models.Chat
import com.example.uvgmarket.chat.models.ParticipantData
import com.example.uvgmarket.data.remote.FirebaseConfig
import com.google.firebase.firestore.FieldValue
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
            Log.d(TAG, "=== INICIANDO CREACIÓN/BÚSQUEDA DE CHAT ===")
            Log.d(TAG, "Usuario actual: $currentUserId")
            Log.d(TAG, "Otro usuario: $otherUserId")

            // Crear lista de participantes ordenada
            val participants = listOf(currentUserId, otherUserId).sorted()
            Log.d(TAG, "Participantes ordenados: $participants")

            // Buscar chat existente
            Log.d(TAG, "Buscando chat existente...")
            val existingChats = chatsCollection
                .whereArrayContains("participants", currentUserId)
                .get()
                .await()

            Log.d(TAG, "Chats encontrados donde participa el usuario: ${existingChats.size()}")

            val existingChat = existingChats.documents.firstOrNull { doc ->
                val chatParticipants = doc.get("participants") as? List<*>
                val sortedChatParticipants = chatParticipants?.mapNotNull { it as? String }?.sorted()

                Log.d(TAG, "Comparando: $sortedChatParticipants con $participants")
                sortedChatParticipants == participants
            }

            if (existingChat != null) {
                Log.d(TAG, "✓ Chat existente encontrado: ${existingChat.id}")
                return existingChat.id
            }

            // Crear nuevo chat
            Log.d(TAG, "No se encontró chat existente. Creando nuevo...")

            val chatData = mapOf(
                "participants" to participants,
                "lastMessage" to "",
                "lastMessageTimestamp" to System.currentTimeMillis(),
                "lastMessageSenderId" to "",
                "participantsData" to mapOf(
                    currentUserId to mapOf(
                        "userId" to currentUserId,
                        "name" to currentUserName,
                        "profileImage" to currentUserImage,
                        "unreadCount" to 0
                    ),
                    otherUserId to mapOf(
                        "userId" to otherUserId,
                        "name" to otherUserName,
                        "profileImage" to otherUserImage,
                        "unreadCount" to 0
                    )
                )
            )

            val docRef = chatsCollection.document()
            docRef.set(chatData).await()

            Log.d(TAG, "✓ Chat creado exitosamente: ${docRef.id}")
            Log.d(TAG, "=== FIN CREACIÓN/BÚSQUEDA DE CHAT ===")

            docRef.id

        } catch (e: Exception) {
            Log.e(TAG, "✗ Error al obtener/crear chat", e)
            Log.e(TAG, "Tipo de error: ${e.javaClass.simpleName}")
            Log.e(TAG, "Mensaje: ${e.message}")
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
            Log.d(TAG, "=== ENVIANDO MENSAJE ===")
            Log.d(TAG, "Chat ID: $chatId")
            Log.d(TAG, "Remitente: $senderId")
            Log.d(TAG, "Receptor: $receiverId")
            Log.d(TAG, "Texto: $text")

            val timestamp = System.currentTimeMillis()

            val message = mapOf(
                "chatId" to chatId,
                "senderId" to senderId,
                "receiverId" to receiverId,
                "text" to text,
                "timestamp" to timestamp,
                "isRead" to false,
                "senderName" to senderName,
                "senderProfileImage" to senderProfileImage
            )

            // Guardar mensaje
            val messageRef = messagesCollection.document()
            messageRef.set(message).await()
            Log.d(TAG, "✓ Mensaje guardado: ${messageRef.id}")

            // Actualizar último mensaje del chat y contador de no leídos
            val chatRef = chatsCollection.document(chatId)

            chatRef.update(
                mapOf(
                    "lastMessage" to text,
                    "lastMessageTimestamp" to timestamp,
                    "lastMessageSenderId" to senderId,
                    "participantsData.$receiverId.unreadCount" to FieldValue.increment(1)
                )
            ).await()

            Log.d(TAG, "✓ Chat actualizado")
            Log.d(TAG, "=== MENSAJE ENVIADO EXITOSAMENTE ===")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error enviando mensaje", e)
            Log.e(TAG, "Tipo de error: ${e.javaClass.simpleName}")
            Log.e(TAG, "Mensaje: ${e.message}")
            false
        }
    }

    /**
     * Obtiene los chats de un usuario
     */
    suspend fun getUserChats(userId: String): List<Chat> {
        return try {
            Log.d(TAG, "=== OBTENIENDO CHATS DEL USUARIO ===")
            Log.d(TAG, "Usuario ID: $userId")

            val snapshot = chatsCollection
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            Log.d(TAG, "Documentos encontrados: ${snapshot.size()}")

            val chats = snapshot.documents.mapNotNull { doc ->
                try {
                    Log.d(TAG, "Procesando chat: ${doc.id}")
                    doc.data?.let { data ->
                        val chat = Chat.fromMap(doc.id, data)
                        Log.d(TAG, "✓ Chat parseado: ${chat.id}, participantes: ${chat.participants}")
                        chat
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "✗ Error parseando chat ${doc.id}", e)
                    null
                }
            }

            Log.d(TAG, "✓ Chats obtenidos: ${chats.size}")
            Log.d(TAG, "=== FIN OBTENCIÓN DE CHATS ===")
            chats
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error obteniendo chats", e)
            Log.e(TAG, "Tipo de error: ${e.javaClass.simpleName}")
            Log.e(TAG, "Mensaje: ${e.message}")
            emptyList()
        }
    }

    /**
     * Marca los mensajes de un chat como leídos
     */
    suspend fun markMessagesAsRead(chatId: String, userId: String): Boolean {
        return try {
            Log.d(TAG, "Marcando mensajes como leídos en chat: $chatId para usuario: $userId")

            // Actualizar contador de no leídos
            chatsCollection.document(chatId)
                .update("participantsData.$userId.unreadCount", 0)
                .await()

            // Marcar mensajes individuales como leídos
            val unreadMessages = messagesCollection
                .whereEqualTo("chatId", chatId)
                .whereEqualTo("receiverId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .await()

            unreadMessages.documents.forEach { doc ->
                doc.reference.update("isRead", true).await()
            }

            Log.d(TAG, "✓ Mensajes marcados como leídos")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error marcando mensajes como leídos", e)
            false
        }
    }
}