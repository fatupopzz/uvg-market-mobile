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
 * Maneja la lógica de validación e inicio de sesión
 */
class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    // Estado privado mutable
    private val _uiState = MutableStateFlow(LoginUiState())

    // Estado público inmutable que observa la UI
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

        // Lanzar coroutine en el scope del ViewModel
        viewModelScope.launch {
            // Observar el Flow del repositorio
            repository.login(correo, contrasena).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = LoginUiState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = LoginUiState(
                            isSuccess = true,
                            user = result.data
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = LoginUiState(
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }
    }

    /**
     * Valida los campos del formulario
     * @return Mensaje de error o null si todo está correcto
     */
    private fun validateFields(correo: String, contrasena: String): String? {
        return when {
            correo.isBlank() -> "El correo no puede estar vacío"
            !correo.contains("@") -> "El correo electrónico no es válido"
            contrasena.isBlank() -> "La contraseña no puede estar vacía"
            else -> null
        }
    }

    /**
     * Verifica si hay un usuario con sesión activa
     */
    fun checkLoginStatus(): Boolean {
        return repository.isUserLoggedIn()
    }

    /**
     * Resetea el estado de error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Resetea todo el estado
     */
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}