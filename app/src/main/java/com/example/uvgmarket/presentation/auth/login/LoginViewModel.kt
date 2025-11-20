package com.example.uvgmarket.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log

/**
 * Estado de la UI para la pantalla de login
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

/**
 * ViewModel para la pantalla de login
 */
class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "LoginViewModel"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Inicia sesión con correo y contraseña
     */
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
                Log.d(TAG, "Iniciando login...")

                // Autenticar con Firebase Auth
                val result = auth.signInWithEmailAndPassword(correo, contrasena).await()
                val uid = result.user?.uid

                if (uid != null) {
                    Log.d(TAG, "Autenticación exitosa: $uid")

                    // Intentar obtener datos de Firestore
                    var user: User? = null
                    try {
                        val document = firestore.collection("users").document(uid).get().await()
                        if (document.exists()) {
                            val data = document.data
                            if (data != null) {
                                user = User.fromMap(data)
                                Log.d(TAG, "Datos de usuario obtenidos de Firestore")
                            }
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "No se pudieron obtener datos de Firestore: ${e.message}")
                    }

                    // Si no hay datos en Firestore, crear usuario básico
                    if (user == null) {
                        user = User(
                            uid = uid,
                            nombre = result.user?.displayName ?: "Usuario",
                            usuario = correo.split("@")[0],
                            correo = correo
                        )
                        Log.d(TAG, "Usuario básico creado")
                    }

                    // Emitir éxito
                    _uiState.value = LoginUiState(
                        isSuccess = true,
                        user = user
                    )
                    Log.d(TAG, "Login completado")
                } else {
                    _uiState.value = LoginUiState(error = "Error al iniciar sesión")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error en login: ${e.message}", e)
                val errorMessage = when {
                    e.message?.contains("no user record") == true ||
                            e.message?.contains("invalid-credential") == true ||
                            e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                            e.message?.contains("wrong-password") == true ||
                            e.message?.contains("invalid-email") == true ->
                        "Correo o contraseña incorrectos"
                    e.message?.contains("network") == true ->
                        "Error de conexión. Verifica tu internet"
                    e.message?.contains("too-many-requests") == true ->
                        "Demasiados intentos. Espera un momento"
                    else -> "Error al iniciar sesión: ${e.message}"
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