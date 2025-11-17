package com.example.uvgmarket.data.remote

import com.example.uvgmarket.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import android.util.Log

/**
 * Servicio que maneja las operaciones con Firebase Authentication y Firestore
 */
class FirebaseAuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val TAG = "FirebaseAuthService"

    init {
        // Habilitar persistencia offline para Firestore
        try {
            firestore.firestoreSettings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        } catch (e: Exception) {
            Log.w(TAG, "Persistencia ya habilitada", e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun registerWithEmail(email: String, password: String): String {
        return try {
            Log.d(TAG, "Creando usuario en Auth...")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Error al crear usuario")
            Log.d(TAG, "Usuario creado exitosamente: $uid")
            uid
        } catch (e: Exception) {
            Log.e(TAG, "Error en registerWithEmail", e)
            throw e
        }
    }

    suspend fun saveUserData(user: User) {
        try {
            Log.d(TAG, "Guardando datos del usuario: ${user.uid}")

            // Usar merge para que no sobrescriba datos existentes
            usersCollection
                .document(user.uid)
                .set(user.toMap(), SetOptions.merge())
                .await()

            Log.d(TAG, "Datos guardados exitosamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error guardando datos", e)
            throw e
        }
    }

    suspend fun loginWithEmail(email: String, password: String): String {
        return try {
            Log.d(TAG, "Iniciando sesión...")
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Error al iniciar sesión")
            Log.d(TAG, "Sesión iniciada: $uid")
            uid
        } catch (e: Exception) {
            Log.e(TAG, "Error en loginWithEmail", e)
            throw e
        }
    }

    suspend fun getUserData(uid: String): User {
        return try {
            Log.d(TAG, "Obteniendo datos del usuario: $uid")
            val document = usersCollection.document(uid).get().await()

            if (!document.exists()) {
                Log.w(TAG, "Usuario no existe en Firestore, creando datos básicos")
                // Si no existe, crear datos básicos
                val basicUser = User(
                    uid = uid,
                    nombre = auth.currentUser?.displayName ?: "Usuario",
                    usuario = auth.currentUser?.email?.split("@")?.get(0) ?: "usuario",
                    correo = auth.currentUser?.email ?: "",
                    imagenPerfil = "",
                    imagenPortada = "",
                    fechaCreacion = System.currentTimeMillis()
                )
                saveUserData(basicUser)
                return basicUser
            }

            val data = document.data ?: throw Exception("Datos de usuario vacíos")
            Log.d(TAG, "Datos obtenidos exitosamente")
            User.fromMap(data)
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo datos", e)
            throw e
        }
    }

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
            Log.e(TAG, "Error verificando usuario", e)
            true // Si hay error, permitir continuar
        }
    }

    fun logout() {
        Log.d(TAG, "Cerrando sesión")
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}