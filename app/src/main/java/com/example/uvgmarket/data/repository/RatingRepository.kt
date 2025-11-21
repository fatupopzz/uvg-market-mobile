package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.data.model.Rating
import com.example.uvgmarket.data.remote.FirebaseConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.delay

class RatingRepository {

    private val firestore: FirebaseFirestore = FirebaseConfig.getFirestore()
    private val ratingsCollection = firestore.collection("ratings")
    private val usersCollection = firestore.collection("users")
    private val TAG = "RatingRepository"

    /**
     * Guarda o actualiza la calificación de un usuario
     */
    suspend fun rateUser(
        fromUserId: String,
        toUserId: String,
        rating: Int
    ): Boolean {
        return try {
            Log.d(TAG, "Calificando usuario $toUserId con $rating estrellas")

            // Buscar si ya existe una calificación de este usuario
            val existingRating = getRatingFromUser(fromUserId, toUserId)

            if (existingRating != null) {
                // Actualizar calificación existente
                Log.d(TAG, "Actualizando calificación existente: ${existingRating.id}")
                ratingsCollection.document(existingRating.id)
                    .update("rating", rating, "timestamp", System.currentTimeMillis())
                    .await()
            } else {
                // Crear nueva calificación
                val docRef = ratingsCollection.document()
                val newRating = Rating(
                    id = docRef.id,
                    fromUserId = fromUserId,
                    toUserId = toUserId,
                    rating = rating,
                    timestamp = System.currentTimeMillis()
                )

                docRef.set(newRating.toMap()).await()
                Log.d(TAG, "Nueva calificación creada: ${docRef.id}")
            }

            // Actualizar el promedio del usuario calificado
            updateUserAverageRating(toUserId)

            // NUEVO: Esperar un poco para que Firebase se actualice
            delay(800)

            Log.d(TAG, "✓ Calificación guardada exitosamente")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error al guardar calificación: ${e.message}", e)
            false
        }
    }

    /**
     * Obtiene la calificación que un usuario le dio a otro
     */
    private suspend fun getRatingFromUser(
        fromUserId: String,
        toUserId: String
    ): Rating? {
        return try {
            val snapshot = ratingsCollection
                .whereEqualTo("fromUserId", fromUserId)
                .whereEqualTo("toUserId", toUserId)
                .limit(1)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents.first()
                Rating.fromMap(doc.id, doc.data ?: emptyMap())
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo calificación: ${e.message}")
            null
        }
    }

    /**
     * Obtiene la calificación que el usuario actual le dio a otro usuario
     */
    suspend fun getUserRating(
        fromUserId: String,
        toUserId: String
    ): Int {
        return try {
            val rating = getRatingFromUser(fromUserId, toUserId)
            rating?.rating ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo calificación del usuario: ${e.message}")
            0
        }
    }

    /**
     * Calcula y actualiza el promedio de calificación de un usuario
     */
    private suspend fun updateUserAverageRating(userId: String) {
        try {
            Log.d(TAG, "Calculando promedio para usuario: $userId")

            // Obtener todas las calificaciones del usuario
            val snapshot = ratingsCollection
                .whereEqualTo("toUserId", userId)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Log.d(TAG, "No hay calificaciones para este usuario")
                // Si no hay calificaciones, dejar en 0
                usersCollection.document(userId)
                    .update("rating", 0)
                    .await()
                return
            }

            // Calcular promedio
            val ratings = snapshot.documents.mapNotNull { doc ->
                (doc.data?.get("rating") as? Long)?.toInt()
            }

            val average = ratings.average()
            val roundedAverage = kotlin.math.round(average).toInt().coerceIn(0, 5)

            Log.d(TAG, "Ratings individuales: $ratings")
            Log.d(TAG, "Promedio calculado: $average -> redondeado: $roundedAverage")

            // Actualizar el rating en el documento del usuario
            usersCollection.document(userId)
                .update("rating", roundedAverage)
                .await()

            Log.d(TAG, "✓ Promedio actualizado en Firestore: $roundedAverage")

            // NUEVO: Pequeño delay para asegurar consistencia
            delay(300)
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando promedio: ${e.message}", e)
        }
    }

    /**
     * Obtiene el promedio de calificación de un usuario desde Firestore
     */
    suspend fun getUserAverageRating(userId: String): Int {
        return try {
            // CAMBIADO: Forzar lectura del servidor, no del cache
            val document = usersCollection.document(userId)
                .get(Source.SERVER)
                .await()

            if (document.exists()) {
                val rating = (document.data?.get("rating") as? Long)?.toInt() ?: 0
                Log.d(TAG, "Promedio de calificación para $userId: $rating")
                rating
            } else {
                Log.w(TAG, "Usuario no encontrado: $userId")
                0
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo promedio: ${e.message}")
            0
        }
    }

    /**
     * Obtiene todas las calificaciones que recibió un usuario
     */
    suspend fun getUserRatings(userId: String): List<Rating> {
        return try {
            val snapshot = ratingsCollection
                .whereEqualTo("toUserId", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    Rating.fromMap(doc.id, doc.data ?: emptyMap())
                } catch (e: Exception) {
                    Log.e(TAG, "Error parseando rating: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo ratings: ${e.message}")
            emptyList()
        }
    }
}