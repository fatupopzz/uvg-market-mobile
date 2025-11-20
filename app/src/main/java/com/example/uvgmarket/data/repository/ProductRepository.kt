package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para manejar operaciones de productos con Firebase Firestore
 */
class ProductRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val productsCollection = firestore.collection("products")
    private val TAG = "ProductRepository"

    /**
     * Obtiene todos los productos activos
     */
    suspend fun getAllProducts(): List<Product> {
        return try {
            Log.d(TAG, "Obteniendo todos los productos...")
            val snapshot = productsCollection
                .whereEqualTo("activo", true)
                .get()
                .await()

            val products = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Product.fromMap(it) }
            }
            Log.d(TAG, "Productos obtenidos: ${products.size}")
            products
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo productos: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Obtiene un producto por su ID
     */
    suspend fun getProductById(productId: String): Product? {
        return try {
            Log.d(TAG, "Obteniendo producto: $productId")
            val document = productsCollection.document(productId).get().await()

            if (document.exists()) {
                document.data?.let { Product.fromMap(it) }
            } else {
                Log.w(TAG, "Producto no encontrado: $productId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo producto: ${e.message}", e)
            null
        }
    }

    /**
     * Obtiene productos de un vendedor específico
     */
    suspend fun getProductsByVendor(vendorId: String): List<Product> {
        return try {
            Log.d(TAG, "Obteniendo productos del vendedor: $vendorId")
            val snapshot = productsCollection
                .whereEqualTo("vendedorId", vendorId)
                .whereEqualTo("activo", true)
                .get()
                .await()

            val products = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Product.fromMap(it) }
            }
            Log.d(TAG, "Productos del vendedor: ${products.size}")
            products
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo productos del vendedor: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Crea un nuevo producto
     */
    suspend fun createProduct(product: Product): Boolean {
        return try {
            Log.d(TAG, "Creando producto: ${product.nombre}")
            val docRef = if (product.id.isNotEmpty()) {
                productsCollection.document(product.id)
            } else {
                productsCollection.document()
            }

            val productWithId = product.copy(id = docRef.id)
            docRef.set(productWithId.toMap()).await()

            Log.d(TAG, "Producto creado: ${docRef.id}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creando producto: ${e.message}", e)
            false
        }
    }

    /**
     * Actualiza un producto existente
     */
    suspend fun updateProduct(product: Product): Boolean {
        return try {
            Log.d(TAG, "Actualizando producto: ${product.id}")
            productsCollection.document(product.id)
                .set(product.toMap())
                .await()

            Log.d(TAG, "Producto actualizado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando producto: ${e.message}", e)
            false
        }
    }

    /**
     * Elimina un producto (soft delete)
     */
    suspend fun deleteProduct(productId: String): Boolean {
        return try {
            Log.d(TAG, "Eliminando producto: $productId")
            productsCollection.document(productId)
                .update("activo", false)
                .await()

            Log.d(TAG, "Producto eliminado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error eliminando producto: ${e.message}", e)
            false
        }
    }

    /**
     * Busca productos por nombre o descripción
     */
    suspend fun searchProducts(query: String): List<Product> {
        return try {
            Log.d(TAG, "Buscando productos: $query")
            // Firestore no soporta búsqueda de texto completo,
            // así que obtenemos todos y filtramos localmente
            val allProducts = getAllProducts()
            val filtered = allProducts.filter { product ->
                product.nombre.contains(query, ignoreCase = true) ||
                        product.descripcion.contains(query, ignoreCase = true)
            }
            Log.d(TAG, "Resultados de búsqueda: ${filtered.size}")
            filtered
        } catch (e: Exception) {
            Log.e(TAG, "Error buscando productos: ${e.message}", e)
            emptyList()
        }
    }
}