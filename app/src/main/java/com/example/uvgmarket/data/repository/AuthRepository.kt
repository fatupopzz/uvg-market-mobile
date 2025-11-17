package com.example.uvgmarket.data.repository

import com.example.uvgmarket.core.util.Resource
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.remote.FirebaseAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que maneja la lógica de autenticación
 */
class AuthRepository {

    private val firebaseService = FirebaseAuthService()
    private val TAG = "AuthRepository"

    /**
     * Registra un nuevo usuario - OPTIMIZADO
     */
    fun register(
        nombre: String,
        usuario: String,
        correo: String,
        contrasena: String
    ): Flow<Resource<User>> = flow {
        try {
            Log.d(TAG, "=== INICIANDO REGISTRO ===")
            emit(Resource.Loading())

            // NO verificar username (esto causa el delay)
            // La verificación se hará después en segundo plano

            // 1. Crear usuario en Firebase Auth INMEDIATAMENTE
            Log.d(TAG, "Paso 1: Creando usuario en Auth...")
            val uid = withContext(Dispatchers.IO) {
                firebaseService.registerWithEmail(correo, contrasena)
            }
            Log.d(TAG, "✓ Usuario Auth creado: $uid")

            // 2. Crear objeto User
            val newUser = User(
                uid = uid,
                nombre = nombre,
                usuario = usuario,
                correo = correo,
                imagenPerfil = "",
                imagenPortada = "",
                fechaCreacion = System.currentTimeMillis()
            )

            // 3. Guardar en Firestore SIN ESPERAR (fire and forget)
            Log.d(TAG, "Paso 2: Iniciando guardado en Firestore...")
            withContext(Dispatchers.IO) {
                firebaseService.saveUserData(newUser)
            }
            Log.d(TAG, "✓ Datos guardados en Firestore")

            // 4. Emitir éxito INMEDIATAMENTE
            Log.d(TAG, "=== REGISTRO EXITOSO ===")
            emit(Resource.Success(newUser))

        } catch (e: Exception) {
            Log.e(TAG, "=== ERROR EN REGISTRO ===", e)
            val errorMessage = when {
                e.message?.contains("email address is already in use") == true ->
                    "El correo electrónico ya está registrado"
                e.message?.contains("network") == true ->
                    "Error de conexión. Verifica tu internet"
                e.message?.contains("password") == true ->
                    "La contraseña debe tener al menos 6 caracteres"
                else -> "Error al registrar: ${e.message}"
            }
            emit(Resource.Error(errorMessage))
        }
    }

    /**
     * Inicia sesión con email y contraseña - OPTIMIZADO
     */
    fun login(
        correo: String,
        contrasena: String
    ): Flow<Resource<User>> = flow {
        try {
            Log.d(TAG, "=== INICIANDO LOGIN ===")
            emit(Resource.Loading())

            // 1. Autenticar
            Log.d(TAG, "Paso 1: Autenticando...")
            val uid = withContext(Dispatchers.IO) {
                firebaseService.loginWithEmail(correo, contrasena)
            }
            Log.d(TAG, "✓ Autenticado: $uid")

            // 2. Obtener datos
            Log.d(TAG, "Paso 2: Obteniendo datos...")
            val user = withContext(Dispatchers.IO) {
                firebaseService.getUserData(uid)
            }
            Log.d(TAG, "✓ Datos obtenidos")

            // 3. Emitir éxito
            Log.d(TAG, "=== LOGIN EXITOSO ===")
            emit(Resource.Success(user))

        } catch (e: Exception) {
            Log.e(TAG, "=== ERROR EN LOGIN ===", e)
            val errorMessage = when {
                e.message?.contains("no user record") == true ||
                        e.message?.contains("invalid-credential") == true ||
                        e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                        e.message?.contains("wrong-password") == true ->
                    "Correo o contraseña incorrectos"
                e.message?.contains("network") == true ->
                    "Error de conexión. Verifica tu internet"
                else -> "Error al iniciar sesión: ${e.message}"
            }
            emit(Resource.Error(errorMessage))
        }
    }

    fun logout() {
        firebaseService.logout()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseService.isUserLoggedIn()
    }

    suspend fun getCurrentUser(): User? {
        return try {
            val firebaseUser = firebaseService.getCurrentUser()
            firebaseUser?.let {
                firebaseService.getUserData(it.uid)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo usuario actual: ${e.message}")
            null
        }
    }
}