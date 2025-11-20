package com.example.uvgmarket.addProd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de agregar producto
 * Conectado a Firebase Firestore
 */
class AgregarProductoViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val auth = FirebaseAuth.getInstance()

    // Estado privado mutable
    private val _uiState = MutableStateFlow(AgregarProductoUiState())

    // Estado público inmutable para la UI
    val uiState: StateFlow<AgregarProductoUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el nombre del producto
     */
    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre, error = null) }
    }

    /**
     * Actualiza la descripción del producto
     */
    fun onDescripcionChange(nuevaDescripcion: String) {
        _uiState.update { it.copy(descripcion = nuevaDescripcion, error = null) }
    }

    /**
     * Actualiza el precio del producto
     */
    fun onPrecioChange(nuevoPrecio: String) {
        _uiState.update { it.copy(precio = nuevoPrecio, error = null) }
    }

    /**
     * Alterna el estado de imagen seleccionada
     */
    fun onImagenClick() {
        _uiState.update { it.copy(imagenSeleccionada = !it.imagenSeleccionada) }
    }

    /**
     * Valida si el formulario está completo para publicar
     */
    fun puedePublicar(): Boolean {
        val state = _uiState.value
        return state.nombre.isNotBlank() &&
                state.descripcion.isNotBlank() &&
                state.precio.isNotBlank()
    }

    /**
     * Publica el producto en Firebase
     */
    fun publicarProducto(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validar campos
        if (!puedePublicar()) {
            _uiState.update { it.copy(error = "Todos los campos son requeridos") }
            return
        }

        // Validar precio
        val precioDouble = state.precio.replace("Q", "").replace(",", "").trim().toDoubleOrNull()
        if (precioDouble == null || precioDouble <= 0) {
            _uiState.update { it.copy(error = "El precio debe ser un número válido") }
            return
        }

        // Obtener usuario actual
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _uiState.update { it.copy(error = "Debes iniciar sesión para publicar") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val product = Product(
                    id = "", // Se generará automáticamente
                    nombre = state.nombre,
                    subtitulo = "", // Puedes agregar este campo al formulario si lo necesitas
                    descripcion = state.descripcion,
                    precio = precioDouble,
                    imagen = if (state.imagenSeleccionada) "product_placeholder" else "product_placeholder",
                    vendedorId = currentUser.uid,
                    vendedorNombre = currentUser.displayName ?: "Vendedor",
                    fechaCreacion = System.currentTimeMillis(),
                    activo = true
                )

                val success = productRepository.createProduct(product)

                if (success) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al publicar el producto"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Limpia todos los campos del formulario
     */
    fun limpiarFormulario() {
        _uiState.value = AgregarProductoUiState()
    }
}

/**
 * Estado de la UI para agregar producto
 */
data class AgregarProductoUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val imagenSeleccionada: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)