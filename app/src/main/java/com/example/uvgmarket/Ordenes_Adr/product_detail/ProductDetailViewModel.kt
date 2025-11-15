package com.example.uvgmarket.Ordenes_Adr.product_detail

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

data class ProductUiState(
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val price: String = ""
)

class ProductDetailViewModel : ViewModel() {

    private val _uiState = mutableStateOf(
        ProductUiState(
            title = "Pandita Hamburguesa",
            subtitle = "Gruesa y caliente",
            description = "Rica hamburguesa libre de gluten sin ningún tipo de preservantes.",
            price = "39.00Q"
        )
    )
    val uiState: State<ProductUiState> = _uiState
}
