package com.example.uvgmarket.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.data.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class ProfileUiState(
    val isLoading: Boolean = false,
    val seller: Seller? = null,
    val productos: List<Product> = emptyList(),
    val error: String? = null
)

class ProfileViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "ProfileViewModel"

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos del usuario actual desde Firebase
     */
    fun loadCurrentUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    Log.d(TAG, "Cargando perfil del usuario: ${currentUser.uid}")

                    // Crear un perfil básico desde Firebase Auth
                    val basicSeller = Seller(
                        id = currentUser.uid,
                        nombre = currentUser.displayName ?: "Usuario",
                        descripcion = currentUser.email ?: "Sin descripción",
                        imagenPerfil = "fotodeperfilindu",
                        imagenPortada = "encabezado_usuario",
                        correo = currentUser.email ?: "",
                        calificacion = 3f
                    )

                    // Cargar productos del usuario desde Firebase
                    val productos = productRepository.getProductsByVendor(currentUser.uid)
                    Log.d(TAG, "Productos cargados: ${productos.size}")

                    _uiState.value = ProfileUiState(
                        seller = basicSeller,
                        productos = productos
                    )
                } else {
                    _uiState.value = ProfileUiState(
                        error = "Usuario no autenticado"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar perfil", e)
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
                Log.d(TAG, "Cargando perfil del usuario: $userId")

                // Crear perfil básico (podrías buscar en Firestore si guardas info adicional)
                val basicSeller = Seller(
                    id = userId,
                    nombre = "Usuario",
                    descripcion = "Vendedor",
                    imagenPerfil = "fotodeperfilhamburger",
                    imagenPortada = "portada_perfil",
                    correo = "",
                    calificacion = 3f
                )

                // Cargar productos del usuario
                val productos = productRepository.getProductsByVendor(userId)
                Log.d(TAG, "Productos cargados: ${productos.size}")

                _uiState.value = ProfileUiState(
                    seller = basicSeller,
                    productos = productos
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar perfil", e)
                _uiState.value = ProfileUiState(
                    error = "Error al cargar el perfil: ${e.message}"
                )
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Eliminando producto: $productId")
                val success = productRepository.deleteProduct(productId)

                if (success) {
                    val productosActualizados = _uiState.value.productos.filter {
                        it.id != productId
                    }

                    _uiState.value = _uiState.value.copy(
                        productos = productosActualizados
                    )
                    Log.d(TAG, "Producto eliminado exitosamente")
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Error al eliminar el producto"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al eliminar producto", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error al eliminar el producto: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = ProfileUiState()
    }
}