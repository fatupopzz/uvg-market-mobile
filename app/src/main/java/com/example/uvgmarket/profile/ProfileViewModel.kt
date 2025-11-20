package com.example.uvgmarket.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.repository.ProductRepository
import com.example.uvgmarket.data.repository.UserRepository
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
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "ProfileViewModel"

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadCurrentUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    Log.d(TAG, "Cargando perfil del usuario: ${currentUser.uid}")

                    // Obtener datos del usuario desde Firestore
                    val user = userRepository.getUserById(currentUser.uid)

                    if (user != null) {
                        // Convertir User a Seller para mantener compatibilidad
                        val seller = Seller(
                            id = user.uid,
                            nombre = user.nombre,
                            descripcion = user.correo,
                            imagenPerfil = user.imagenPerfil.ifEmpty { "fotodeperfilindu" },
                            imagenPortada = user.imagenPortada.ifEmpty { "encabezado_usuario" },
                            correo = user.correo,
                            calificacion = user.rating.toFloat()
                        )

                        // Cargar productos del usuario
                        val productos = productRepository.getProductsByVendor(currentUser.uid)
                        Log.d(TAG, "Datos cargados - Productos: ${productos.size}")

                        _uiState.value = ProfileUiState(
                            seller = seller,
                            productos = productos
                        )
                    } else {
                        Log.w(TAG, "Usuario no encontrado en Firestore, creando perfil básico")
                        // Crear perfil básico si no existe en Firestore
                        val basicUser = User(
                            uid = currentUser.uid,
                            nombre = currentUser.displayName ?: "Usuario",
                            usuario = currentUser.email?.split("@")?.get(0) ?: "usuario",
                            correo = currentUser.email ?: "",
                            imagenPerfil = "fotodeperfilindu",
                            imagenPortada = "encabezado_usuario"
                        )
                        userRepository.createUser(basicUser)

                        val seller = Seller(
                            id = basicUser.uid,
                            nombre = basicUser.nombre,
                            descripcion = basicUser.correo,
                            imagenPerfil = "fotodeperfilindu",
                            imagenPortada = "encabezado_usuario",
                            correo = basicUser.correo,
                            calificacion = 3f
                        )

                        _uiState.value = ProfileUiState(
                            seller = seller,
                            productos = emptyList()
                        )
                    }
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

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)

            try {
                Log.d(TAG, "Cargando perfil del usuario: $userId")

                // Obtener datos del usuario desde Firestore
                val user = userRepository.getUserById(userId)

                if (user != null) {
                    val seller = Seller(
                        id = user.uid,
                        nombre = user.nombre,
                        descripcion = user.correo,
                        imagenPerfil = user.imagenPerfil.ifEmpty { "fotodeperfilhamburger" },
                        imagenPortada = user.imagenPortada.ifEmpty { "portada_perfil" },
                        correo = user.correo,
                        calificacion = user.rating.toFloat()
                    )

                    val productos = productRepository.getProductsByVendor(userId)
                    Log.d(TAG, "Productos cargados: ${productos.size}")

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