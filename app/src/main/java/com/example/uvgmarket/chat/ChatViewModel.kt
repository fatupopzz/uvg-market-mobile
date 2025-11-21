package com.example.uvgmarket.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.chat.components.Message
import com.example.uvgmarket.data.repository.ChatRepository
import com.example.uvgmarket.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val messageText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val chatId: String? = null,
    val otherUserName: String = "",
    val otherUserImage: String = ""
)

class ChatViewModel : ViewModel() {

    private val chatRepository = ChatRepository()
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "ChatViewModel"

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    /**
     * Inicializa el chat con otro usuario
     */
    fun initializeChat(otherUserId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                    return@launch
                }

                // Obtener datos del usuario actual
                val currentUserData = userRepository.getUserById(currentUser.uid)
                val currentUserName = currentUserData?.nombre ?: "Usuario"
                val currentUserImage = currentUserData?.imagenPerfil ?: ""

                // Obtener datos del otro usuario
                val otherUserData = userRepository.getUserById(otherUserId)
                val otherUserName = otherUserData?.nombre ?: "Usuario"
                val otherUserImage = otherUserData?.imagenPerfil ?: ""

                // Obtener o crear chat
                val chatId = chatRepository.getOrCreateChat(
                    currentUserId = currentUser.uid,
                    otherUserId = otherUserId,
                    currentUserName = currentUserName,
                    currentUserImage = currentUserImage,
                    otherUserName = otherUserName,
                    otherUserImage = otherUserImage
                )

                if (chatId != null) {
                    _uiState.value = _uiState.value.copy(
                        chatId = chatId,
                        otherUserName = otherUserName,
                        otherUserImage = otherUserImage,
                        isLoading = false
                    )

                    // Escuchar mensajes en tiempo real
                    listenToMessages(chatId)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al crear el chat"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error inicializando chat", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Escucha los mensajes en tiempo real
     */
    private fun listenToMessages(chatId: String) {
        viewModelScope.launch {
            chatRepository.listenToMessages(chatId).collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    /**
     * Actualiza el texto del mensaje
     */
    fun onMessageTextChange(newText: String) {
        _uiState.value = _uiState.value.copy(messageText = newText)
    }

    /**
     * Envía un mensaje
     */
    fun sendMessage(receiverId: String) {
        val state = _uiState.value
        val text = state.messageText.trim()

        if (text.isEmpty() || state.chatId == null) return

        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                val currentUserData = userRepository.getUserById(currentUser.uid)
                val success = chatRepository.sendMessage(
                    chatId = state.chatId,
                    senderId = currentUser.uid,
                    receiverId = receiverId,
                    text = text,
                    senderName = currentUserData?.nombre ?: "Usuario",
                    senderProfileImage = currentUserData?.imagenPerfil ?: ""
                )

                if (success) {
                    _uiState.value = _uiState.value.copy(messageText = "")
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Error al enviar el mensaje"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error enviando mensaje", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}