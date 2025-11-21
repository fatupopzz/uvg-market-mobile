// Reemplaza el archivo app/src/main/java/com/example/uvgmarket/profile/ProfileViewModel.kt

package com.example.uvgmarket.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.repository.ProductRepository
import com.example.uvgmarket.data.repository.UserRepository
import com.example.uvgmarket.data.repository.RatingRepository
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
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val productToDelete: Product? = null,
    val deleteSuccess: String? = null,
    val currentUserRating: Int = 0,  // NUEVO: Rating que el usuario actual le dio
    val showRatingDialog: Boolean = false  // NUEVO: Controlar el diálogo
)

class ProfileViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val userRepository = UserRepository()
    private val ratingRepository = RatingRepository()  // NUEVO
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

                    val user = userRepository.getUserById(currentUser.uid)

                    if (user != null) {
                        val seller = Seller(
                            id = user.uid,
                            nombre = user.nombre,
                            descripcion = user.correo,
                            imagenPerfil = user.imagenPerfil.ifEmpty { "fotodeperfilindu" },
                            imagenPortada = user.imagenPortada.ifEmpty { "encabezado_usuario" },
                            correo = user.correo,
                            calificacion = user.rating.toFloat()
                        )

                        val productos = productRepository.getProductsByVendor(currentUser.uid)
                        Log.d(TAG, "Datos cargados - Productos: ${productos.size}")

                        _uiState.value = ProfileUiState(
                            seller = seller,
                            productos = productos,
                            currentUserRating = 0  // No aplica para perfil propio
                        )
                    } else {
                        Log.w(TAG, "Usuario no encontrado en Firestore, creando perfil básico")
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

                val currentUser = auth.currentUser
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

                    // NUEVO: Obtener la calificación que el usuario actual le dio a este usuario
                    val currentUserRating = if (currentUser != null) {
                        ratingRepository.getUserRating(currentUser.uid, userId)
                    } else {
                        0
                    }

                    Log.d(TAG, "Rating actual del usuario: $currentUserRating")

                    _uiState.value = ProfileUiState(
                        seller = seller,
                        productos = productos,
                        currentUserRating = currentUserRating
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

    // NUEVO: Mostrar diálogo de calificación
    fun showRatingDialog() {
        _uiState.value = _uiState.value.copy(showRatingDialog = true)
    }

    // NUEVO: Ocultar diálogo de calificación
    fun hideRatingDialog() {
        _uiState.value = _uiState.value.copy(showRatingDialog = false)
    }

    // NUEVO: Calificar usuario
    fun rateUser(toUserId: String, rating: Int) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    Log.d(TAG, "Calificando usuario $toUserId con $rating estrellas")

                    _uiState.value = _uiState.value.copy(isLoading = true)

                    val success = ratingRepository.rateUser(
                        fromUserId = currentUser.uid,
                        toUserId = toUserId,
                        rating = rating
                    )

                    if (success) {
                        Log.d(TAG, "✓ Calificación guardada exitosamente")

                        // Actualizar el UI con la nueva calificación
                        _uiState.value = _uiState.value.copy(
                            currentUserRating = rating,
                            isLoading = false,
                            showRatingDialog = false
                        )

                        // Recargar el perfil para obtener el promedio actualizado
                        loadUserProfile(toUserId)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Error al guardar la calificación"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al calificar usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    // Resto de funciones existentes...

    fun showDeleteDialog(product: Product) {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            productToDelete = product
        )
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            productToDelete = null
        )
    }

    fun confirmDeleteProduct() {
        val product = _uiState.value.productToDelete ?: return

        viewModelScope.launch {
            try {
                Log.d(TAG, "Eliminando producto: ${product.id}")

                _uiState.value = _uiState.value.copy(isLoading = true)

                val success = productRepository.deleteProduct(product.id)

                if (success) {
                    val productosActualizados = _uiState.value.productos.filter {
                        it.id != product.id
                    }

                    _uiState.value = _uiState.value.copy(
                        productos = productosActualizados,
                        showDeleteDialog = false,
                        productToDelete = null,
                        isLoading = false,
                        deleteSuccess = "Producto eliminado exitosamente"
                    )

                    Log.d(TAG, "Producto eliminado exitosamente")

                    kotlinx.coroutines.delay(3000)
                    _uiState.value = _uiState.value.copy(deleteSuccess = null)
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Error al eliminar el producto",
                        showDeleteDialog = false,
                        productToDelete = null,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al eliminar producto", e)
                _uiState.value = _uiState.value.copy(
                    error = "Error al eliminar el producto: ${e.message}",
                    showDeleteDialog = false,
                    productToDelete = null,
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearDeleteSuccess() {
        _uiState.value = _uiState.value.copy(deleteSuccess = null)
    }

    fun resetState() {
        _uiState.value = ProfileUiState()
    }

    fun refresh() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loadCurrentUserProfile()
        }
    }
}