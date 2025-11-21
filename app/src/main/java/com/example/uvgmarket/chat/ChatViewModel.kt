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
                Log.d(TAG, "=== INICIALIZANDO CHAT ===")
                _uiState.value = _uiState.value.copy(isLoading = true)

                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                    return@launch
                }

                Log.d(TAG, "Usuario actual: ${currentUser.uid}")
                Log.d(TAG, "Otro usuario: $otherUserId")

                // Obtener datos del usuario actual
                val currentUserData = userRepository.getUserById(currentUser.uid)
                val currentUserName = currentUserData?.nombre ?: "Usuario"
                val currentUserImage = currentUserData?.imagenPerfil ?: ""

                Log.d(TAG, "Datos usuario actual - Nombre: $currentUserName")

                // Obtener datos del otro usuario
                val otherUserData = userRepository.getUserById(otherUserId)
                val otherUserName = otherUserData?.nombre ?: "Usuario"
                val otherUserImage = otherUserData?.imagenPerfil ?: ""

                Log.d(TAG, "Datos otro usuario - Nombre: $otherUserName")

                // Obtener o crear chat
                Log.d(TAG, "Obteniendo o creando chat...")
                val chatId = chatRepository.getOrCreateChat(
                    currentUserId = currentUser.uid,
                    otherUserId = otherUserId,
                    currentUserName = currentUserName,
                    currentUserImage = currentUserImage,
                    otherUserName = otherUserName,
                    otherUserImage = otherUserImage
                )

                if (chatId != null) {
                    Log.d(TAG, "✓ Chat obtenido/creado: $chatId")

                    _uiState.value = _uiState.value.copy(
                        chatId = chatId,
                        otherUserName = otherUserName,
                        otherUserImage = otherUserImage,
                        isLoading = false,
                        error = null
                    )

                    // Marcar mensajes como leídos
                    chatRepository.markMessagesAsRead(chatId, currentUser.uid)

                    // Escuchar mensajes en tiempo real
                    listenToMessages(chatId)

                    Log.d(TAG, "=== CHAT INICIALIZADO EXITOSAMENTE ===")
                } else {
                    Log.e(TAG, "✗ Error: chatId es null")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al crear el chat"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "✗ Error inicializando chat", e)
                Log.e(TAG, "Tipo: ${e.javaClass.simpleName}, Mensaje: ${e.message}")
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
            try {
                Log.d(TAG, "Iniciando escucha de mensajes para chat: $chatId")
                chatRepository.listenToMessages(chatId).collect { messages ->
                    Log.d(TAG, "Mensajes actualizados: ${messages.size}")
                    _uiState.value = _uiState.value.copy(
                        messages = messages,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en listenToMessages", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error cargando mensajes: ${e.message}"
                )
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

        if (text.isEmpty() || state.chatId == null) {
            Log.w(TAG, "No se puede enviar: texto vacío o chatId null")
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e(TAG, "No se puede enviar: usuario no autenticado")
            return
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "Enviando mensaje...")

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
                    Log.d(TAG, "✓ Mensaje enviado exitosamente")
                    _uiState.value = _uiState.value.copy(
                        messageText = "",
                        error = null
                    )
                } else {
                    Log.e(TAG, "✗ Error al enviar mensaje")
                    _uiState.value = _uiState.value.copy(
                        error = "Error al enviar el mensaje"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "✗ Excepción enviando mensaje", e)
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