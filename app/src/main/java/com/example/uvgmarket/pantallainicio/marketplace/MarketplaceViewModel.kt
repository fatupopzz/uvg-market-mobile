package com.example.uvgmarket.pantallainicio.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.Seller
import com.example.uvgmarket.data.repository.SellerRepository
import com.example.uvgmarket.pantallainicio.components.Entrepreneur
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para MarketplaceScreen
 */
data class MarketplaceUiState(
    val isLoading: Boolean = false,
    val entrepreneurs: List<Entrepreneur> = emptyList(),
    val allEntrepreneurs: List<Entrepreneur> = emptyList(),
    val searchText: String = "",
    val error: String? = null,
    val isSearching: Boolean = false
)

/**
 * ViewModel para la pantalla de Marketplace
 * Ahora conectado a Firebase Firestore
 */
class MarketplaceViewModel : ViewModel() {

    private val sellerRepository = SellerRepository()

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState.asStateFlow()

    init {
        loadEntrepreneurs()
    }

    /**
     * Carga la lista de emprendedores desde Firebase
     */
    private fun loadEntrepreneurs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val sellers = sellerRepository.getAllSellers()
                val entrepreneurs = sellers.map { seller ->
                    seller.toEntrepreneur()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    entrepreneurs = entrepreneurs,
                    allEntrepreneurs = entrepreneurs
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar emprendedores: ${e.message}"
                )
            }
        }
    }

    /**
     * Actualiza el texto de búsqueda y filtra en tiempo real
     */
    fun onSearchTextChange(newText: String) {
        _uiState.value = _uiState.value.copy(searchText = newText)
        performSearch(newText)
    }

    /**
     * Realiza la búsqueda cuando se presiona el botón de búsqueda
     */
    fun onSearchClick() {
        performSearch(_uiState.value.searchText)
    }

    /**
     * Lógica de búsqueda
     */
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

    /**
     * Limpia la búsqueda y restaura la lista completa
     */
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchText = "",
            entrepreneurs = _uiState.value.allEntrepreneurs,
            isSearching = false
        )
    }

    /**
     * Recarga los emprendedores desde Firebase
     */
    fun refresh() {
        loadEntrepreneurs()
    }

    /**
     * Limpia el error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Extensión para convertir Seller a Entrepreneur
     */
    private fun Seller.toEntrepreneur(): Entrepreneur {
        return Entrepreneur(
            name = this.nombre,
            description = this.descripcion,
            rating = this.calificacion.toInt(),
            profileImage = this.imagenPerfil,
            productImages = this.productImages
        )
    }
}