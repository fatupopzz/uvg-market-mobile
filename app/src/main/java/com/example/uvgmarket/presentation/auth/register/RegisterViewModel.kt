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
 * Maneja la lógica de validación y registro de usuarios
 */
class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    // Estado privado mutable
    private val _uiState = MutableStateFlow(RegisterUiState())

    // Estado público inmutable que observa la UI
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Registra un nuevo usuario
     */
    fun register(nombre: String, usuario: String, correo: String, contrasena: String, confirmarContrasena: String) {
        // Validar campos antes de hacer la petición
        val validationError = validateFields(nombre, usuario, correo, contrasena, confirmarContrasena)
        if (validationError != null) {
            _uiState.value = RegisterUiState(error = validationError)
            return
        }

        // Lanzar coroutine en el scope del ViewModel
        viewModelScope.launch {
            // Observar el Flow del repositorio
            repository.register(nombre, usuario, correo, contrasena).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = RegisterUiState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = RegisterUiState(
                            isSuccess = true,
                            user = result.data
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = RegisterUiState(
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
        _uiState.value = RegisterUiState()
    }
}