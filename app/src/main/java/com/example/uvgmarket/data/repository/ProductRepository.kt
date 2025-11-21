package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.data.model.Product
import com.example.uvgmarket.data.remote.FirebaseConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private val firestore: FirebaseFirestore = FirebaseConfig.getFirestore()
    private val productsCollection = firestore.collection("products")
    private val TAG = "ProductRepository"

    // Caché para productos
    private var allProductsCache: Pair<List<Product>, Long>? = null
    private val CACHE_DURATION = 2 * 60 * 1000L // 2 minutos

    /**
     * Obtiene todos los productos activos
     * Usa caché y Firestore offline
     */
    suspend fun getAllProducts(): List<Product> {
        return try {
            // 1. Verificar caché
            val cached = allProductsCache
            if (cached != null) {
                val (products, timestamp) = cached
                if (System.currentTimeMillis() - timestamp < CACHE_DURATION) {
                    Log.d(TAG, "✓ Productos obtenidos de caché (${products.size})")
                    return products
                }
            }

            Log.d(TAG, "Obteniendo productos de Firestore...")

            // 2. Intentar caché de Firestore primero
            var snapshot = try {
                productsCollection
                    .whereEqualTo("activo", true)
                    .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                    .limit(50) // Limitar a 50 productos más recientes
                    .get(Source.CACHE)
                    .await()
            } catch (e: Exception) {
                // Si no hay caché, obtener del servidor
                productsCollection
                    .whereEqualTo("activo", true)
                    .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                    .limit(50)
                    .get(Source.SERVER)
                    .await()
            }

            val products = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data?.let {
                        Product.fromMap(it).copy(id = doc.id)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando producto: ${e.message}")
                    null
                }
            }

            // Guardar en caché
            allProductsCache = Pair(products, System.currentTimeMillis())

            Log.d(TAG, "✓ ${products.size} productos obtenidos")
            products
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error obteniendo productos: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Obtiene un producto por su ID
     */
    suspend fun getProductById(productId: String): Product? {
        return try {
            Log.d(TAG, "Obteniendo producto: $productId")

            // Intentar caché primero
            val document = try {
                productsCollection.document(productId)
                    .get(Source.CACHE)
                    .await()
            } catch (e: Exception) {
                productsCollection.document(productId)
                    .get(Source.SERVER)
                    .await()
            }

            if (document.exists()) {
                document.data?.let {
                    Product.fromMap(it).copy(id = document.id)
                }
            } else {
                Log.w(TAG, "Producto no encontrado: $productId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error obteniendo producto: ${e.message}", e)
            null
        }
    }

    /**
     * Obtiene productos de un vendedor específico
     */
    suspend fun getProductsByVendor(vendorId: String): List<Product> {
        return try {
            Log.d(TAG, "Obteniendo productos del vendedor: $vendorId")

            // Intentar caché primero
            val snapshot = try {
                productsCollection
                    .whereEqualTo("vendedorId", vendorId)
                    .whereEqualTo("activo", true)
                    .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                    .get(Source.CACHE)
                    .await()
            } catch (e: Exception) {
                productsCollection
                    .whereEqualTo("vendedorId", vendorId)
                    .whereEqualTo("activo", true)
                    .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                    .get(Source.SERVER)
                    .await()
            }

            val products = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data?.let {
                        Product.fromMap(it).copy(id = doc.id)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando producto: ${e.message}")
                    null
                }
            }

            Log.d(TAG, "✓ ${products.size} productos del vendedor")
            products
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error obteniendo productos del vendedor: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Crea un nuevo producto
     */
    suspend fun createProduct(product: Product): Boolean {
        return try {
            Log.d(TAG, "Creando producto: ${product.nombre}")

            val docRef = productsCollection.document()
            val productWithId = product.copy(id = docRef.id)

            docRef.set(productWithId.toMap()).await()

            // Invalidar caché para forzar recarga
            allProductsCache = null

            Log.d(TAG, "✓ Producto creado: ${docRef.id}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error creando producto: ${e.message}", e)
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

            // Invalidar caché
            allProductsCache = null

            Log.d(TAG, "✓ Producto actualizado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error actualizando producto: ${e.message}", e)
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

            // Invalidar caché
            allProductsCache = null

            Log.d(TAG, "✓ Producto eliminado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error eliminando producto: ${e.message}", e)
            false
        }
    }

    /**
     * Limpia la caché
     */
    fun clearCache() {
        allProductsCache = null
        Log.d(TAG, "Caché limpiada")
    }
}