package com.example.uvgmarket.domain.repository

import com.example.uvgmarket.domain.model.Chat
import com.example.uvgmarket.chat.components.Message

/**
 * Interfaz del repositorio de chats
 */
interface ChatRepository {

    suspend fun getAllChats(): Result<List<Chat>>

    suspend fun getChatById(chatId: String): Result<Chat>

    suspend fun getMessages(chatId: String): Result<List<Message>>

    suspend fun sendMessage(message: Message): Result<String>

    suspend fun markAsRead(chatId: String): Result<Unit>

    suspend fun searchChats(query: String): Result<List<Chat>>

    suspend fun getOrCreateChat(userId: String): Result<Chat>
}
