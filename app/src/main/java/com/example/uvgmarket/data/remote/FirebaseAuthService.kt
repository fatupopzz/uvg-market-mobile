package com.example.uvgmarket.data.remote

import com.example.uvgmarket.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Servicio que maneja las operaciones con Firebase Authentication y Firestore
 */
class FirebaseAuthService {

    // Instancias de Firebase
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Referencia a la colección de usuarios en Firestore
    private val usersCollection = firestore.collection("users")

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun registerWithEmail(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Error al crear usuario")
    }

    suspend fun saveUserData(user: User) {
        usersCollection.document(user.uid).set(user.toMap()).await()
    }

    suspend fun loginWithEmail(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Error al iniciar sesión")
    }

    suspend fun getUserData(uid: String): User {
        val document = usersCollection.document(uid).get().await()
        val data = document.data ?: throw Exception("Usuario no encontrado")
        return User.fromMap(data)
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        val query = usersCollection
            .whereEqualTo("usuario", username)
            .limit(1)
            .get()
            .await()
        return query.isEmpty
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
