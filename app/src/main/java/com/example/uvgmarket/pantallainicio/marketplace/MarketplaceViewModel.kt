package com.example.uvgmarket.pantallainicio.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.repository.ProductRepository
import com.example.uvgmarket.data.repository.UserRepository
import com.example.uvgmarket.pantallainicio.components.Entrepreneur
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class MarketplaceUiState(
    val isLoading: Boolean = false,
    val entrepreneurs: List<Entrepreneur> = emptyList(),
    val allEntrepreneurs: List<Entrepreneur> = emptyList(),
    val searchText: String = "",
    val error: String? = null,
    val isSearching: Boolean = false
)

class MarketplaceViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val userRepository = UserRepository()
    private val TAG = "MarketplaceViewModel"

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState.asStateFlow()

    init {
        loadEntrepreneurs()
    }

    /**
     * Carga emprendedores agrupando productos por vendedor desde Firebase
     * CON RATINGS REALES
     */
    fun loadEntrepreneurs(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log.d(TAG, "Cargando productos desde Firebase... (forceRefresh: $forceRefresh)")

                // Obtener todos los productos
                val allProducts = productRepository.getAllProducts()
                Log.d(TAG, "Productos obtenidos: ${allProducts.size}")

                // Agrupar productos por vendedor
                val productsByVendor = allProducts.groupBy { it.vendedorId }

                // Crear un Entrepreneur por cada vendedor con rating real
                val entrepreneurs = productsByVendor.map { (vendedorId, products) ->
                    val firstProduct = products.first()

                    // CAMBIADO: Usar forceRefresh para obtener datos actualizados
                    val user = userRepository.getUserById(vendedorId, forceRefresh = forceRefresh)
                    val rating = user?.rating ?: 0
                    val profileImage = user?.imagenPerfil?.ifEmpty { "fotodeperfilindu" } ?: "fotodeperfilindu"

                    Log.d(TAG, "Vendedor: ${firstProduct.vendedorNombre}, Rating: $rating")

                    // Tomar los primeros 2 productos para mostrar
                    val displayProducts = products.take(2)

                    Entrepreneur(
                        id = vendedorId,
                        name = firstProduct.vendedorNombre,
                        description = "Vendedor con ${products.size} producto(s)",
                        rating = rating,
                        profileImage = profileImage,
                        productImages = displayProducts.map { it.imagen },
                        productIds = displayProducts.map { it.id }
                    )
                }

                Log.d(TAG, "✓ Emprendedores creados: ${entrepreneurs.size}")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    entrepreneurs = entrepreneurs,
                    allEntrepreneurs = entrepreneurs
                )
            } catch (e: Exception) {
                Log.e(TAG, "✗ Error al cargar emprendedores", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar emprendedores: ${e.message}"
                )
            }
        }
    }

    fun onSearchTextChange(newText: String) {
        _uiState.value = _uiState.value.copy(searchText = newText)
        performSearch(newText)
    }

    fun onSearchClick() {
        performSearch(_uiState.value.searchText)
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            val trimmedQuery = query.trim()

            if (trimmedQuery.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    entrepreneurs = _uiState.value.allEntrepreneurs,
                    isSearching = false
                )
            } else {
                val filtered = _uiState.value.allEntrepreneurs.filter { entrepreneur ->
                    entrepreneur.name.contains(trimmedQuery, ignoreCase = true) ||
                            entrepreneur.description.contains(trimmedQuery, ignoreCase = true)
                }

                _uiState.value = _uiState.value.copy(
                    entrepreneurs = filtered,
                    isSearching = true
                )
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchText = "",
            entrepreneurs = _uiState.value.allEntrepreneurs,
            isSearching = false
        )
    }

    fun refresh() {
        Log.d(TAG, "Refrescando marketplace con forceRefresh=true")
        productRepository.clearCache()
        userRepository.clearCache()
        loadEntrepreneurs(forceRefresh = true)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}