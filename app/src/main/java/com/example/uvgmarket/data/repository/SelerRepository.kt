package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.data.model.Seller
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para manejar operaciones de vendedores con Firebase Firestore
 */
class SellerRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val sellersCollection = firestore.collection("sellers")
    private val TAG = "SellerRepository"

    /**
     * Obtiene todos los vendedores activos
     */
    suspend fun getAllSellers(): List<Seller> {
        return try {
            Log.d(TAG, "Obteniendo todos los vendedores...")
            val snapshot = sellersCollection
                .whereEqualTo("activo", true)
                .get()
                .await()

            val sellers = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Seller.fromMap(it) }
            }
            Log.d(TAG, "Vendedores obtenidos: ${sellers.size}")
            sellers
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo vendedores: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Obtiene un vendedor por su ID
     */
    suspend fun getSellerById(sellerId: String): Seller? {
        return try {
            Log.d(TAG, "Obteniendo vendedor: $sellerId")
            val document = sellersCollection.document(sellerId).get().await()

            if (document.exists()) {
                document.data?.let { Seller.fromMap(it) }
            } else {
                Log.w(TAG, "Vendedor no encontrado: $sellerId")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo vendedor: ${e.message}", e)
            null
        }
    }

    /**
     * Crea un nuevo vendedor
     */
    suspend fun createSeller(seller: Seller): Boolean {
        return try {
            Log.d(TAG, "Creando vendedor: ${seller.nombre}")
            val docRef = if (seller.id.isNotEmpty()) {
                sellersCollection.document(seller.id)
            } else {
                sellersCollection.document()
            }

            val sellerWithId = seller.copy(id = docRef.id)
            docRef.set(sellerWithId.toMap()).await()

            Log.d(TAG, "Vendedor creado: ${docRef.id}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creando vendedor: ${e.message}", e)
            false
        }
    }

    /**
     * Actualiza un vendedor existente
     */
    suspend fun updateSeller(seller: Seller): Boolean {
        return try {
            Log.d(TAG, "Actualizando vendedor: ${seller.id}")
            sellersCollection.document(seller.id)
                .set(seller.toMap())
                .await()

            Log.d(TAG, "Vendedor actualizado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando vendedor: ${e.message}", e)
            false
        }
    }

    /**
     * Actualiza la calificación de un vendedor
     */
    suspend fun updateRating(sellerId: String, newRating: Float): Boolean {
        return try {
            Log.d(TAG, "Actualizando calificación de $sellerId a $newRating")
            sellersCollection.document(sellerId)
                .update("calificacion", newRating)
                .await()

            Log.d(TAG, "Calificación actualizada")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando calificación: ${e.message}", e)
            false
        }
    }

    /**
     * Busca vendedores por nombre o descripción
     */
    suspend fun searchSellers(query: String): List<Seller> {
        return try {
            Log.d(TAG, "Buscando vendedores: $query")
            val allSellers = getAllSellers()
            val filtered = allSellers.filter { seller ->
                seller.nombre.contains(query, ignoreCase = true) ||
                        seller.descripcion.contains(query, ignoreCase = true)
            }
            Log.d(TAG, "Resultados de búsqueda: ${filtered.size}")
            filtered
        } catch (e: Exception) {
            Log.e(TAG, "Error buscando vendedores: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Elimina un vendedor (soft delete)
     */
    suspend fun deleteSeller(sellerId: String): Boolean {
        return try {
            Log.d(TAG, "Eliminando vendedor: $sellerId")
            sellersCollection.document(sellerId)
                .update("activo", false)
                .await()

            Log.d(TAG, "Vendedor eliminado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error eliminando vendedor: ${e.message}", e)
            false
        }
    }
}