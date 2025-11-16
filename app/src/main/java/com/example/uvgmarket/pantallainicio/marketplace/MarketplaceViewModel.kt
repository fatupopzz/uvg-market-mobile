package com.example.uvgmarket.pantallainicio.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.pantallainicio.components.Entrepreneur
import com.example.uvgmarket.R
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
    val searchText: String = "",
    val error: String? = null
)

/**
 * ViewModel para la pantalla de Marketplace
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
                    entrepreneurs = entrepreneurs
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
     * Actualiza el texto de búsqueda
     */
    fun onSearchTextChange(newText: String) {
        _uiState.value = _uiState.value.copy(searchText = newText)
    }

    /**
     * Realiza la búsqueda
     */
    fun onSearchClick() {
        // TODO: Implementar lógica de búsqueda
        val searchText = _uiState.value.searchText
        if (searchText.isNotBlank()) {
            // Filtrar emprendedores por nombre o descripción
            val filtered = _uiState.value.entrepreneurs.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.description.contains(searchText, ignoreCase = true)
            }
            _uiState.value = _uiState.value.copy(entrepreneurs = filtered)
        } else {
            loadEntrepreneurs()
        }
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