package com.example.uvgmarket.pantallainicio.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.repository.ProductRepository
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
    private val TAG = "MarketplaceViewModel"

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState.asStateFlow()

    init {
        loadEntrepreneurs()
    }

    /**
     * Carga emprendedores agrupando productos por vendedor desde Firebase
     */
    private fun loadEntrepreneurs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log.d(TAG, "Cargando productos desde Firebase...")

                // Obtener todos los productos
                val allProducts = productRepository.getAllProducts()
                Log.d(TAG, "Productos obtenidos: ${allProducts.size}")

                // Agrupar productos por vendedor
                val productsByVendor = allProducts.groupBy { it.vendedorId }

                // Crear un Entrepreneur por cada vendedor
                val entrepreneurs = productsByVendor.map { (vendedorId, products) ->
                    val firstProduct = products.first()

                    Entrepreneur(
                        id = vendedorId, // NUEVO: Agregar ID del vendedor
                        name = firstProduct.vendedorNombre,
                        description = "Vendedor con ${products.size} producto(s)",
                        rating = 3, // Rating por defecto
                        profileImage = "fotodeperfilindu", // Imagen por defecto
                        productImages = products.take(2).map { it.imagen }
                    )
                }

                Log.d(TAG, "Emprendedores creados: ${entrepreneurs.size}")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    entrepreneurs = entrepreneurs,
                    allEntrepreneurs = entrepreneurs
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar emprendedores", e)
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
        productRepository.clearCache()
        loadEntrepreneurs()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}