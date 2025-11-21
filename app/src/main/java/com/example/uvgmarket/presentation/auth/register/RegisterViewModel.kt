package com.example.uvgmarket.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.core.constants.ValidationConstants
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()
    private val TAG = "RegisterViewModel"

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(
        nombre: String,
        usuario: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ) {
        val validationError = validateFields(nombre, usuario, correo, contrasena, confirmarContrasena)
        if (validationError != null) {
            _uiState.value = RegisterUiState(error = validationError)
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = RegisterUiState(isLoading = true)
                Log.d(TAG, "=== INICIANDO REGISTRO ===")

                // 1. Crear usuario en Firebase Auth
                Log.d(TAG, "Paso 1: Creando usuario en Auth...")
                val result = withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(correo, contrasena).await()
                }
                val uid = result.user?.uid

                if (uid != null) {
                    Log.d(TAG, "✓ Usuario creado en Auth: $uid")

                    // 2. Crear objeto User
                    val newUser = User(
                        uid = uid,
                        nombre = nombre,
                        usuario = usuario,
                        correo = correo,
                        imagenPerfil = "fotodeperfilindu",
                        imagenPortada = "encabezado_usuario",
                        fechaCreacion = System.currentTimeMillis()
                    )

                    // 3. Guardar en Firestore (sin esperar - fire and forget)
                    Log.d(TAG, "Paso 2: Guardando en Firestore (background)...")
                    launch(Dispatchers.IO) {
                        try {
                            userRepository.createUser(newUser)
                            Log.d(TAG, "✓ Usuario guardado en Firestore")
                        } catch (e: Exception) {
                            Log.e(TAG, "⚠ Error guardando en Firestore (no crítico): ${e.message}")
                        }
                    }

                    // 4. Emitir éxito INMEDIATAMENTE
                    Log.d(TAG, "=== REGISTRO EXITOSO ===")
                    _uiState.value = RegisterUiState(
                        isSuccess = true,
                        user = newUser
                    )
                } else {
                    _uiState.value = RegisterUiState(error = "Error al crear usuario")
                }

            } catch (e: Exception) {
                Log.e(TAG, "=== ERROR EN REGISTRO ===", e)
                val errorMessage = when {
                    e.message?.contains("email address is already in use") == true ->
                        "El correo electrónico ya está registrado"
                    e.message?.contains("badly formatted") == true ->
                        "El formato del correo no es válido"
                    e.message?.contains("weak-password") == true ||
                            e.message?.contains("password") == true ->
                        "La contraseña debe tener al menos 6 caracteres"
                    e.message?.contains("network") == true ->
                        "Error de conexión. Verifica tu internet"
                    else -> "Error al registrar: ${e.message}"
                }
                _uiState.value = RegisterUiState(error = errorMessage)
            }
        }
    }

    private fun validateFields(
        nombre: String,
        usuario: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ): String? {
        return when {
            nombre.isBlank() -> "El nombre no puede estar vacío"
            usuario.isBlank() -> "El usuario no puede estar vacío"
            usuario.length < ValidationConstants.MIN_USERNAME_LENGTH ->
                "El usuario debe tener al menos ${ValidationConstants.MIN_USERNAME_LENGTH} caracteres"
            usuario.length > ValidationConstants.MAX_USERNAME_LENGTH ->
                "El usuario debe tener máximo ${ValidationConstants.MAX_USERNAME_LENGTH} caracteres"
            correo.isBlank() -> "El correo no puede estar vacío"
            !ValidationConstants.EMAIL_REGEX.matches(correo) ->
                "El correo electrónico no es válido"
            contrasena.isBlank() -> "La contraseña no puede estar vacía"
            contrasena.length < ValidationConstants.MIN_PASSWORD_LENGTH ->
                "La contraseña debe tener al menos ${ValidationConstants.MIN_PASSWORD_LENGTH} caracteres"
            contrasena != confirmarContrasena -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}