package com.example.uvgmarket.chat_general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.chat_general.models.Chat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para PantallaChatGeneral
 */
data class ChatGeneralUiState(
    val isLoading: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val searchText: String = "",
    val error: String? = null
)

/**
 * ViewModel para la pantalla de chat general
 */
class ChatGeneralViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatGeneralUiState())
    val uiState: StateFlow<ChatGeneralUiState> = _uiState.asStateFlow()

    init {
        loadChats()
    }

    /**
     * Carga la lista de chats
     */
    private fun loadChats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: Reemplazar con llamada a repositorio/Firebase
                val chats = getHardcodedChats()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    chats = chats
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar chats: ${e.message}"
                )
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
            loadChats()
        } else {
            val allChats = getHardcodedChats()
            val filtered = allChats.filter {
                it.nombreContacto.contains(newText, ignoreCase = true) ||
                        it.ultimoMensaje.contains(newText, ignoreCase = true)
            }
            _uiState.value = _uiState.value.copy(chats = filtered)
        }
    }

    /**
     * Limpia el error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Datos hardcodeados (temporal)
     * TODO: Mover a repositorio
     */
    private fun getHardcodedChats(): List<Chat> {
        return listOf(
            Chat(
                id = "1",
                nombreContacto = "Hamburguesas kawaii",
                ultimoMensaje = "no se, ahorita te digo.",
                hora = "2:14 PM",
                imagenPerfil = "fotodeperfilhamburger",
                mensajesNoLeidos = 1
            ),
            Chat(
                id = "2",
                nombreContacto = "Grupo de Ux Lab",
                ultimoMensaje = "Fatima hacete shhhh",
                hora = "2:59 PM",
                imagenPerfil = "fotodeperfiljoyeria",
                mensajesNoLeidos = 1
            )
        )
    }
}