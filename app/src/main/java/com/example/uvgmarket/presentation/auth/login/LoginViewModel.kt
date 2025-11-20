package com.example.uvgmarket.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()
    private val TAG = "LoginViewModel"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(correo: String, contrasena: String) {
        // Validar campos
        val validationError = validateFields(correo, contrasena)
        if (validationError != null) {
            _uiState.value = LoginUiState(error = validationError)
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = LoginUiState(isLoading = true)
                Log.d(TAG, "=== INICIANDO LOGIN ===")

                // 1. Autenticar con Firebase Auth
                Log.d(TAG, "Paso 1: Autenticando con correo: $correo")
                val result = withContext(Dispatchers.IO) {
                    auth.signInWithEmailAndPassword(correo, contrasena).await()
                }
                val uid = result.user?.uid

                if (uid != null) {
                    Log.d(TAG, "✓ Autenticación exitosa: $uid")

                    // 2. Intentar obtener datos de Firestore (sin bloquear)
                    var user: User? = null
                    try {
                        user = withContext(Dispatchers.IO) {
                            userRepository.getUserById(uid)
                        }
                        if (user != null) {
                            Log.d(TAG, "✓ Datos de usuario obtenidos de Firestore")
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "⚠ No se pudieron obtener datos de Firestore: ${e.message}")
                    }

                    // 3. Si no hay datos en Firestore, crear usuario básico
                    if (user == null) {
                        Log.d(TAG, "Creando perfil básico...")
                        user = User(
                            uid = uid,
                            nombre = result.user?.displayName ?: "Usuario",
                            usuario = correo.split("@")[0],
                            correo = correo,
                            imagenPerfil = "fotodeperfilindu",
                            imagenPortada = "encabezado_usuario"
                        )

                        // Intentar guardar en background
                        launch(Dispatchers.IO) {
                            try {
                                userRepository.createUser(user)
                                Log.d(TAG, "✓ Perfil básico guardado")
                            } catch (e: Exception) {
                                Log.e(TAG, "⚠ Error guardando perfil básico: ${e.message}")
                            }
                        }
                    }

                    // 4. Emitir éxito
                    Log.d(TAG, "=== LOGIN EXITOSO ===")
                    _uiState.value = LoginUiState(
                        isSuccess = true,
                        user = user
                    )
                } else {
                    _uiState.value = LoginUiState(error = "Error al iniciar sesión")
                }

            } catch (e: Exception) {
                Log.e(TAG, "=== ERROR EN LOGIN ===", e)
                Log.e(TAG, "Tipo de error: ${e.javaClass.simpleName}")
                Log.e(TAG, "Mensaje de error: ${e.message}")

                val errorMessage = when {
                    e.message?.contains("no user record") == true ||
                            e.message?.contains("invalid-credential") == true ||
                            e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                            e.message?.contains("INVALID_EMAIL") == true ||
                            e.message?.contains("wrong-password") == true ||
                            e.message?.contains("invalid-email") == true ->
                        "Correo o contraseña incorrectos"
                    e.message?.contains("network") == true ->
                        "Error de conexión. Verifica tu internet"
                    e.message?.contains("too-many-requests") == true ->
                        "Demasiados intentos. Espera un momento"
                    else -> "Error al iniciar sesión. Verifica tus credenciales"
                }
                _uiState.value = LoginUiState(error = errorMessage)
            }
        }
    }

    private fun validateFields(correo: String, contrasena: String): String? {
        return when {
            correo.isBlank() -> "El correo no puede estar vacío"
            !correo.contains("@") -> "El correo electrónico no es válido"
            contrasena.isBlank() -> "La contraseña no puede estar vacía"
            else -> null
        }
    }

    fun checkLoginStatus(): Boolean {
        return auth.currentUser != null
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}