package com.example.uvgmarket.pantallainicio.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val allEntrepreneurs: List<Entrepreneur> = emptyList(), // Lista completa sin filtrar
    val searchText: String = "",
    val error: String? = null,
    val isSearching: Boolean = false // Indica si hay una búsqueda activa
)

/**
 * ViewModel para la pantalla de Marketplace
 * Ahora con búsqueda funcional en tiempo real
 */
class MarketplaceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState.asStateFlow()

    init {
        loadEntrepreneurs()
    }

    /**
     * Carga la lista de emprendedores
     */
    private fun loadEntrepreneurs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: Reemplazar con llamada a repositorio/Firebase
                val entrepreneurs = getHardcodedEntrepreneurs()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    entrepreneurs = entrepreneurs,
                    allEntrepreneurs = entrepreneurs // Guardar lista completa para búsquedas
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

        // Filtrar en tiempo real mientras el usuario escribe
        performSearch(newText)
    }

    /**
     * Realiza la búsqueda cuando se presiona el botón de búsqueda
     */
    fun onSearchClick() {
        performSearch(_uiState.value.searchText)
    }

    /**
     * Lógica de búsqueda reutilizable
     */
    private fun performSearch(query: String) {
        viewModelScope.launch {
            val trimmedQuery = query.trim()

            if (trimmedQuery.isBlank()) {
                // Si la búsqueda está vacía, mostrar todos los emprendedores
                _uiState.value = _uiState.value.copy(
                    entrepreneurs = _uiState.value.allEntrepreneurs,
                    isSearching = false
                )
            } else {
                // Filtrar emprendedores por nombre o descripción
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
     * Limpia el error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Datos hardcodeados (temporal)
     * TODO: Mover a repositorio
     */
    private fun getHardcodedEntrepreneurs(): List<Entrepreneur> {
        return listOf(
            Entrepreneur(
                name = "Hamburguesas kawaii",
                description = "Tu lugar fav para comer",
                rating = 3,
                profileImage = "fotodeperfilhamburger",
                productImages = listOf("hamburger1", "hamburger2")
            ),
            Entrepreneur(
                name = "Accesorios Luna",
                description = "Joyería artesanal hecha a mano",
                rating = 5,
                profileImage = "fotodeperfiljoyeria",
                productImages = listOf("joyeria1", "joyeria2")
            ),
            Entrepreneur(
                name = "TechRepair GT",
                description = "Reparación de celulares y laptops",
                rating = 4,
                profileImage = "imagendeperfilcomputadora",
                productImages = listOf("limpinado1", "limpiando2")
            )
        )
    }
}