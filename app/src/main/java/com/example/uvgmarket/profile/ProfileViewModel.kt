package com.example.uvgmarket.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.profile.models.Usuario
import com.example.uvgmarket.profile.models.Producto
import com.example.uvgmarket.profile.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla de perfil
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val usuario: Usuario? = null,
    val productos: List<Producto> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel para la pantalla de perfil
 * Maneja la lógica de carga de datos del usuario y sus productos
 */
class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos del usuario actual (perfil propio)
     */
    fun loadCurrentUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                val usuario = UserRepository.getCurrentUser()
                val productos = UserRepository.getCurrentUserProducts()

                _uiState.value = ProfileUiState(
                    usuario = usuario,
                    productos = productos
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(
                    error = "Error al cargar el perfil: ${e.message}"
                )
            }
        }
    }

    /**
     * Carga los datos de un usuario específico por su ID
     */
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                val usuario = UserRepository.getUserById(userId)

                if (usuario != null) {
                    val productos = UserRepository.getProductsByUserId(userId)

                    _uiState.value = ProfileUiState(
                        usuario = usuario,
                        productos = productos
                    )
                } else {
                    _uiState.value = ProfileUiState(
                        error = "Usuario no encontrado"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(
                    error = "Error al cargar el perfil: ${e.message}"
                )
            }
        }
    }

    /**
     * Elimina un producto de la lista
     */
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                // TODO: Implementar eliminación en Firebase
                val productosActualizados = _uiState.value.productos.filter {
                    it.id != productId
                }

                _uiState.value = _uiState.value.copy(
                    productos = productosActualizados
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al eliminar el producto: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Resetea el estado
     */
    fun resetState() {
        _uiState.value = ProfileUiState()
    }
}