package com.example.uvgmarket.chat

import androidx.lifecycle.ViewModel
import com.example.uvgmarket.chat.components.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {

    // Lista de mensajes (flujo observable)
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // Texto actual del input
    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText

    init {
        // Carga inicial (mock)
        loadMockMessages()
    }

    private fun loadMockMessages() {
        _messages.value = listOf(
            Message(
                id = "1",
                senderId = "otherUser",
                receiverId = "currentUser",
                text = "Hola! ¿Tienes hamburguesas disponibles?",
                senderName = "Hamburguesas kawaii"
            ),
            Message(
                id = "2",
                senderId = "currentUser",
                receiverId = "otherUser",
                text = "Sí, tenemos varias opciones",
                senderName = "Usuario"
            )
        )
    }

    fun onMessageTextChange(newText: String) {
        _messageText.value = newText
    }

    fun sendMessage(currentUserId: String, recipientId: String) {
        val text = _messageText.value.trim()
        if (text.isEmpty()) return

        val newMessage = Message(
            id = System.currentTimeMillis().toString(),
            senderId = currentUserId,
            receiverId = recipientId,
            text = text,
            senderName = "Usuario" // temporal
        )

        // Agregar nuevo mensaje a la lista local
        _messages.value = _messages.value + newMessage

        // Limpiar campo
        _messageText.value = ""
    }
}