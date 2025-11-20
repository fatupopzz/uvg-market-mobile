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
import android.util.Log

class AgregarProductoViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "AgregarProductoVM"

    private val _uiState = MutableStateFlow(AgregarProductoUiState())
    val uiState: StateFlow<AgregarProductoUiState> = _uiState.asStateFlow()

    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre, error = null) }
    }

    fun onDescripcionChange(nuevaDescripcion: String) {
        _uiState.update { it.copy(descripcion = nuevaDescripcion, error = null) }
    }

    fun onPrecioChange(nuevoPrecio: String) {
        _uiState.update { it.copy(precio = nuevoPrecio, error = null) }
    }

    fun onImagenClick() {
        _uiState.update { it.copy(imagenSeleccionada = !it.imagenSeleccionada) }
    }

    fun puedePublicar(): Boolean {
        val state = _uiState.value
        return state.nombre.isNotBlank() &&
                state.descripcion.isNotBlank() &&
                state.precio.isNotBlank() &&
                !state.isLoading // Evitar múltiples clics
    }

    fun publicarProducto(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Prevenir múltiples llamadas
        if (state.isLoading) {
            Log.w(TAG, "Ya hay una publicación en proceso")
            return
        }

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

        val currentUser = auth.currentUser
        if (currentUser == null) {
            _uiState.update { it.copy(error = "Debes iniciar sesión para publicar") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                Log.d(TAG, "Creando producto: ${state.nombre}")

                val product = Product(
                    id = "", // Se generará automáticamente en Firestore
                    nombre = state.nombre,
                    subtitulo = state.nombre,
                    descripcion = state.descripcion,
                    precio = precioDouble,
                    imagen = "product_placeholder",
                    vendedorId = currentUser.uid,
                    vendedorNombre = currentUser.displayName ?: currentUser.email?.split("@")?.get(0) ?: "Usuario",
                    fechaCreacion = System.currentTimeMillis(),
                    activo = true
                )

                val success = productRepository.createProduct(product)

                if (success) {
                    Log.d(TAG, "Producto creado exitosamente")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }

                    // Llamar al callback de éxito
                    onSuccess()

                    // Limpiar el estado DESPUÉS de navegar
                    limpiarFormulario()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al publicar el producto"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al crear producto", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = AgregarProductoUiState()
    }
}

data class AgregarProductoUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val imagenSeleccionada: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)