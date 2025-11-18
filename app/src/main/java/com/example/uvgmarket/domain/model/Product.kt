package com.example.uvgmarket.domain.model

/**
 * Modelo de dominio para Producto
 * Representa un producto en el sistema
 */
data class Product(
    val id: String,
    val nombre: String,
    val subtitulo: String,
    val descripcion: String,
    val precio: Double,
    val imagen: Int,
    val vendedorId: String,
    val vendedorNombre: String,
    val categoriaId: String = "",
    val stock: Int = 0,
    val fechaCreacion: Long = System.currentTimeMillis()
) {
    /**
     * Valida si el producto está disponible para compra
     */
    fun isAvailable(): Boolean = stock > 0

    /**
     * Formatea el precio con el símbolo de Quetzal
     */
    fun getFormattedPrice(): String = "Q${String.format("%.2f", precio)}"
}
