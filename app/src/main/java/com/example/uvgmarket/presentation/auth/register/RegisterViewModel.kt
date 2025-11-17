package com.example.uvgmarket.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.core.constants.ValidationConstants
import com.example.uvgmarket.core.util.Resource
import com.example.uvgmarket.data.model.User
import com.example.uvgmarket.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

/**
 * Estado de la UI para la pantalla de registro
 */
data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

/**
 * ViewModel para la pantalla de registro
 */
class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val TAG = "RegisterViewModel"

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Registra un nuevo usuario
     */
    fun register(
        nombre: String,
        usuario: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ) {
        // Validar campos
        val validationError = validateFields(nombre, usuario, correo, contrasena, confirmarContrasena)
        if (validationError != null) {
            _uiState.value = RegisterUiState(error = validationError)
            return
        }

        viewModelScope.launch {
            repository.register(nombre, usuario, correo, contrasena).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Estado: Cargando")
                        _uiState.value = RegisterUiState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "Estado: Éxito - Usuario: ${result.data?.nombre}")
                        _uiState.value = RegisterUiState(
                            isSuccess = true,
                            user = result.data
                        )
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Estado: Error - ${result.message}")
                        _uiState.value = RegisterUiState(
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
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