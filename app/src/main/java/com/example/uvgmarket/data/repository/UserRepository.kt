package com.example.uvgmarket.data.repository

import android.util.Log
import com.example.uvgmarket.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para manejar operaciones de usuarios con Firebase Firestore
 */
class UserRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val TAG = "UserRepository"

    /**
     * Crea un nuevo usuario en Firestore
     */
    suspend fun createUser(user: User): Boolean {
        return try {
            Log.d(TAG, "Creando usuario en Firestore: ${user.uid}")
            usersCollection.document(user.uid)
                .set(user.toMap())
                .await()
            Log.d(TAG, "Usuario creado exitosamente")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error creando usuario: ${e.message}", e)
            false
        }
    }

    /**
     * Obtiene un usuario por su UID
     */
    suspend fun getUserById(uid: String): User? {
        return try {
            Log.d(TAG, "Obteniendo usuario: $uid")
            val document = usersCollection.document(uid).get().await()

            if (document.exists()) {
                val data = document.data
                if (data != null) {
                    User.fromMap(data)
                } else {
                    null
                }
            } else {
                Log.w(TAG, "Usuario no encontrado: $uid")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo usuario: ${e.message}", e)
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
            Log.d(TAG, "Usuario actualizado exitosamente")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando usuario: ${e.message}", e)
            false
        }
    }

    /**
     * Verifica si un nombre de usuario ya existe
     */
    suspend fun isUsernameAvailable(username: String): Boolean {
        return try {
            Log.d(TAG, "Verificando disponibilidad de usuario: $username")
            val query = usersCollection
                .whereEqualTo("usuario", username)
                .limit(1)
                .get()
                .await()
            val available = query.isEmpty
            Log.d(TAG, "Usuario disponible: $available")
            available
        } catch (e: Exception) {
            Log.e(TAG, "Error verificando usuario: ${e.message}", e)
            true // Si hay error, permitir continuar
        }
    }
}