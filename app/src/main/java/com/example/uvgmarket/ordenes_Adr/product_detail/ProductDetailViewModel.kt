package com.example.uvgmarket.ordenes_Adr.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para ProductDetailScreen
 */
data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null
)

/**
 * ViewModel para la pantalla de detalle de producto
 * Conectado a Firebase Firestore
 */
class ProductDetailViewModel : ViewModel() {

    private val productRepository = ProductRepository()

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /**
     * Carga los detalles de un producto por su ID
     */
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState(isLoading = true)

            try {
                val product = productRepository.getProductById(productId)

                if (product != null) {
                    _uiState.value = ProductDetailUiState(product = product)
                } else {
                    _uiState.value = ProductDetailUiState(
                        error = "Producto no encontrado"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState(
                    error = "Error al cargar el producto: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpia el error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}