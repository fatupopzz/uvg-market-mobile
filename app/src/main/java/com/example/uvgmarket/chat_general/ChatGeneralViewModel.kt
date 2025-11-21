package com.example.uvgmarket.chat_general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.chat_general.models.Chat
import com.example.uvgmarket.data.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Estado de la UI para PantallaChatGeneral
 */
data class ChatGeneralUiState(
    val isLoading: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val allChats: List<Chat> = emptyList(),
    val searchText: String = "",
    val error: String? = null
)

/**
 * ViewModel para la pantalla de chat general
 */
class ChatGeneralViewModel : ViewModel() {

    private val chatRepository = ChatRepository()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "ChatGeneralViewModel"

    private val _uiState = MutableStateFlow(ChatGeneralUiState())
    val uiState: StateFlow<ChatGeneralUiState> = _uiState.asStateFlow()

    init {
        loadChats()
    }

    /**
     * Carga la lista de chats reales de Firebase
     */
    private fun loadChats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                    return@launch
                }

                Log.d(TAG, "Cargando chats del usuario: ${currentUser.uid}")

                // Obtener chats de Firebase
                val firebaseChats = chatRepository.getUserChats(currentUser.uid)

                Log.d(TAG, "Chats obtenidos: ${firebaseChats.size}")

                // Convertir a formato de UI
                val uiChats = firebaseChats.map { firebaseChat ->
                    // Obtener el ID del otro usuario
                    val otherUserId = firebaseChat.participants.firstOrNull { it != currentUser.uid } ?: ""

                    // Obtener datos del otro participante
                    val otherUserData = firebaseChat.participantsData[otherUserId]

                    Chat(
                        id = otherUserId, // Usar el ID del otro usuario para navegación
                        nombreContacto = otherUserData?.name ?: "Usuario",
                        ultimoMensaje = firebaseChat.lastMessage.ifEmpty { "Sin mensajes" },
                        hora = formatTimestamp(firebaseChat.lastMessageTimestamp),
                        imagenPerfil = otherUserData?.profileImage ?: "fotodeperfilindu",
                        mensajesNoLeidos = otherUserData?.unreadCount ?: 0
                    )
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    chats = uiChats,
                    allChats = uiChats
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar chats", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar chats: ${e.message}"
                )
            }
        }
    }

    /**
     * Formatea el timestamp a formato de hora
     */
    private fun formatTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Ahora" // Menos de 1 minuto
            diff < 3600_000 -> "${diff / 60_000} min" // Menos de 1 hora
            diff < 86400_000 -> { // Menos de 24 horas
                val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
            diff < 604800_000 -> { // Menos de 7 días
                val sdf = SimpleDateFormat("EEE h:mm a", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
            else -> { // Más de 7 días
                val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }

    /**
     * Actualiza el texto de búsqueda
     */
    fun onSearchTextChange(newText: String) {
        _uiState.value = _uiState.value.copy(searchText = newText)

        // Filtrar chats en tiempo real
        if (newText.isBlank()) {
            _uiState.value = _uiState.value.copy(chats = _uiState.value.allChats)
        } else {
            val filtered = _uiState.value.allChats.filter {
                it.nombreContacto.contains(newText, ignoreCase = true) ||
                        it.ultimoMensaje.contains(newText, ignoreCase = true)
            }
            _uiState.value = _uiState.value.copy(chats = filtered)
        }
    }

    /**
     * Refresca la lista de chats
     */
    fun refresh() {
        loadChats()
    }

    /**
     * Limpia el error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}