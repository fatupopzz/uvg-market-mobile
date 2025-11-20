package com.example.uvgmarket.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.data.repository.ProductRepository
import com.example.uvgmarket.data.repository.SellerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla de perfil
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val seller: Seller? = null,
    val productos: List<Product> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel para la pantalla de perfil
 * Conectado a Firebase Firestore
 */
class ProfileViewModel : ViewModel() {

    private val sellerRepository = SellerRepository()
    private val productRepository = ProductRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos del usuario actual (perfil propio)
     */
    fun loadCurrentUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                val currentUserId = auth.currentUser?.uid

                if (currentUserId != null) {
                    // Buscar si el usuario es un vendedor
                    val seller = sellerRepository.getSellerById(currentUserId)

                    if (seller != null) {
                        val productos = productRepository.getProductsByVendor(currentUserId)

                        _uiState.value = ProfileUiState(
                            seller = seller,
                            productos = productos
                        )
                    } else {
                        // Si no es vendedor, crear un perfil básico desde Auth
                        val basicSeller = Seller(
                            id = currentUserId,
                            nombre = auth.currentUser?.displayName ?: "Usuario",
                            descripcion = "Sin descripción",
                            imagenPerfil = "fotodeperfilindu",
                            imagenPortada = "encabezado_usuario",
                            correo = auth.currentUser?.email ?: ""
                        )

                        _uiState.value = ProfileUiState(
                            seller = basicSeller,
                            productos = emptyList()
                        )
                    }
                } else {
                    _uiState.value = ProfileUiState(
                        error = "Usuario no autenticado"
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
     * Carga los datos de un usuario específico por su ID
     */
    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                val seller = sellerRepository.getSellerById(userId)

                if (seller != null) {
                    val productos = productRepository.getProductsByVendor(userId)

                    _uiState.value = ProfileUiState(
                        seller = seller,
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
                val success = productRepository.deleteProduct(productId)

                if (success) {
                    // Actualizar la lista local
                    val productosActualizados = _uiState.value.productos.filter {
                        it.id != productId
                    }

                    _uiState.value = _uiState.value.copy(
                        productos = productosActualizados
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Error al eliminar el producto"
                    )
                }
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