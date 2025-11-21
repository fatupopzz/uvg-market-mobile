package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.remote.FirebaseConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore: FirebaseFirestore = FirebaseConfig.getFirestore()
    private val usersCollection = firestore.collection("users")
    private val TAG = "UserRepository"

    // Caché en memoria para usuarios frecuentes
    private val userCache = mutableMapOf<String, Pair<User, Long>>()
    private val CACHE_DURATION = 5 * 60 * 1000L // 5 minutos

    /**
     * Crea un nuevo usuario en Firestore
     */
    suspend fun createUser(user: User): Boolean {
        return try {
            Log.d(TAG, "Creando usuario: ${user.uid}")

            usersCollection.document(user.uid)
                .set(user.toMap())
                .await()

            // Actualizar caché
            userCache[user.uid] = Pair(user, System.currentTimeMillis())

            Log.d(TAG, "✓ Usuario creado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error creando usuario: ${e.message}", e)
            false
        }
    }

    /**
     * Obtiene un usuario por su UID
     * Usa caché local primero, luego Firestore
     */
    suspend fun getUserById(uid: String): User? {
        return try {
            // 1. Verificar caché en memoria
            val cached = userCache[uid]
            if (cached != null) {
                val (user, timestamp) = cached
                if (System.currentTimeMillis() - timestamp < CACHE_DURATION) {
                    Log.d(TAG, "✓ Usuario obtenido de caché: $uid")
                    return user
                } else {
                    userCache.remove(uid)
                }
            }

            Log.d(TAG, "Obteniendo usuario de Firestore: $uid")

            // 2. Intentar obtener de caché de Firestore primero
            var document = usersCollection.document(uid)
                .get(Source.CACHE)
                .await()

            // 3. Si no está en caché, obtener del servidor
            if (!document.exists()) {
                document = usersCollection.document(uid)
                    .get(Source.SERVER)
                    .await()
            }

            if (document.exists()) {
                val data = document.data
                if (data != null) {
                    val user = User.fromMap(data)
                    // Guardar en caché
                    userCache[uid] = Pair(user, System.currentTimeMillis())
                    Log.d(TAG, "✓ Usuario obtenido: ${user.nombre}")
                    return user
                }
            }

            Log.w(TAG, "Usuario no encontrado: $uid")
            null
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error obteniendo usuario: ${e.message}", e)
            null
        }
    }

    /**
     * Actualiza los datos de un usuario
     */
    suspend fun updateUser(user: User): Boolean {
        return try {
            Log.d(TAG, "Actualizando usuario: ${user.uid}")

            usersCollection.document(user.uid)
                .set(user.toMap())
                .await()

            // Actualizar caché
            userCache[user.uid] = Pair(user, System.currentTimeMillis())

            Log.d(TAG, "✓ Usuario actualizado")
            true
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error actualizando usuario: ${e.message}", e)
            false
        }
    }

    /**
     * Limpia la caché (útil al hacer logout)
     */
    fun clearCache() {
        userCache.clear()
        Log.d(TAG, "Caché limpiada")
    }
}