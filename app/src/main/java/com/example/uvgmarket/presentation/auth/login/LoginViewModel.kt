package com.example.uvgmarket.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.core.util.Resource
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val repository = AuthRepository()
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
            repository.login(correo, contrasena).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Estado: Cargando")
                        _uiState.value = LoginUiState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "Estado: Éxito - Usuario: ${result.data?.nombre}")
                        _uiState.value = LoginUiState(
                            isSuccess = true,
                            user = result.data
                        )
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Estado: Error - ${result.message}")
                        _uiState.value = LoginUiState(
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
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
        return repository.isUserLoggedIn()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}