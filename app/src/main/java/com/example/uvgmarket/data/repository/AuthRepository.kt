package com.example.uvgmarket.data.repository

import com.example.uvgmarket.core.util.Resource
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.remote.FirebaseAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio que maneja la lógica de autenticación
 * Actúa como intermediario entre el ViewModel y el servicio de Firebase
 * Convierte las operaciones de Firebase en Flows de Resource
 */
class AuthRepository {

    private val firebaseService = FirebaseAuthService()

    /**
     * Registra un nuevo usuario
     * @return Flow con el estado de la operación (Loading, Success, Error)
     */
    fun register(
        nombre: String,
        usuario: String,
        correo: String,
        contrasena: String
    ): Flow<Resource<User>> = flow {
        try {
            // Emitir estado de carga
            emit(Resource.Loading())

            // Verificar si el username ya existe
            val isUsernameAvailable = firebaseService.isUsernameAvailable(usuario)
            if (!isUsernameAvailable) {
                emit(Resource.Error("El nombre de usuario ya está en uso"))
                return@flow
            }

            // Crear usuario en Firebase Auth
            val uid = firebaseService.registerWithEmail(correo, contrasena)

            // Crear objeto User con los datos
            val newUser = User(
                uid = uid,
                nombre = nombre,
                usuario = usuario,
                correo = correo,
                imagenPerfil = "", // Imagen por defecto
                imagenPortada = "", // Imagen por defecto
                fechaCreacion = System.currentTimeMillis()
            )

            // Guardar datos adicionales en Firestore
            firebaseService.saveUserData(newUser)

            // Emitir estado de éxito
            emit(Resource.Success(newUser))

        } catch (e: Exception) {
            // Emitir estado de error
            val errorMessage = when {
                e.message?.contains("email address is already in use") == true ->
                    "El correo electrónico ya está registrado"
                e.message?.contains("network error") == true ->
                    "Error de conexión. Verifica tu internet"
                e.message?.contains("password") == true ->
                    "La contraseña debe tener al menos 6 caracteres"
                else -> e.message ?: "Error desconocido al registrar usuario"
            }
            emit(Resource.Error(errorMessage))
        }
    }

    /**
     * Inicia sesión con email y contraseña
     * @return Flow con el estado de la operación
     */
    fun login(
        correo: String,
        contrasena: String
    ): Flow<Resource<User>> = flow {
        try {
            // Emitir estado de carga
            emit(Resource.Loading())

            // Iniciar sesión en Firebase Auth
            val uid = firebaseService.loginWithEmail(correo, contrasena)

            // Obtener datos del usuario desde Firestore
            val user = firebaseService.getUserData(uid)

            // Emitir estado de éxito
            emit(Resource.Success(user))

        } catch (e: Exception) {
            // Emitir estado de error
            val errorMessage = when {
                e.message?.contains("no user record") == true ||
                        e.message?.contains("invalid-credential") == true ||
                        e.message?.contains("wrong-password") == true ->
                    "Correo o contraseña incorrectos"
                e.message?.contains("network error") == true ->
                    "Error de conexión. Verifica tu internet"
                else -> e.message ?: "Error desconocido al iniciar sesión"
            }
            emit(Resource.Error(errorMessage))
        }
    }

    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        firebaseService.logout()
    }

    /**
     * Verifica si hay un usuario autenticado
     */
    fun isUserLoggedIn(): Boolean {
        return firebaseService.isUserLoggedIn()
    }

    /**
     * Obtiene el usuario actual (si existe)
     */
    suspend fun getCurrentUser(): User? {
        return try {
            val firebaseUser = firebaseService.getCurrentUser()
            firebaseUser?.let {
                firebaseService.getUserData(it.uid)
            }
        } catch (e: Exception) {
            null
        }
    }
}