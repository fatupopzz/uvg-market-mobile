package com.example.uvgmarket.domain.repository

import com.example.uvgmarket.domain.model.Product

/**
 * Interfaz del repositorio de productos
 * Define las operaciones que se pueden realizar con productos
 * Siguiendo el principio de Dependency Inversion (SOLID)
 */
interface ProductRepository {

    /**
     * Obtiene un producto por su ID
     */
    suspend fun getProductById(productId: String): Result<Product>

    /**
     * Obtiene todos los productos disponibles
     */
    suspend fun getAllProducts(): Result<List<Product>>

    /**
     * Obtiene productos de un vendedor específico
     */
    suspend fun getProductsByVendor(vendorId: String): Result<List<Product>>

    /**
     * Busca productos por nombre o descripción
     */
    suspend fun searchProducts(query: String): Result<List<Product>>

    /**
     * Crea un nuevo producto
     */
    suspend fun createProduct(product: Product): Result<String>

    /**
     * Actualiza un producto existente
     */
    suspend fun updateProduct(product: Product): Result<Unit>

    /**
     * Elimina un producto por su ID
     */
    suspend fun deleteProduct(productId: String): Result<Unit>
}
