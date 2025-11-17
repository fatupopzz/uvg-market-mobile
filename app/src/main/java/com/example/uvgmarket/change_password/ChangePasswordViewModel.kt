package com.example.uvgmarket.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.core.constants.ValidationConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val validationErrors: List<String> = emptyList()
)


class ChangePasswordViewModel : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(ChangePasswordUiState())

    // Estado público inmutable que observa la UI
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()


    fun changePassword(
        contrasenaActual: String,
        nuevaContrasena: String,
        confirmarContrasena: String
    ) {
        // Validar campos antes de hacer la petición
        val validationErrors = validateFields(contrasenaActual, nuevaContrasena, confirmarContrasena)
        if (validationErrors.isNotEmpty()) {
            _uiState.value = ChangePasswordUiState(validationErrors = validationErrors)
            return
        }

        // Lanzar coroutine en el scope del ViewModel
        viewModelScope.launch {
            try {
                _uiState.value = ChangePasswordUiState(isLoading = true)

                // TODO: Implementar lógica con Firebase Authentication
                // Por ahora simulamos un delay
                kotlinx.coroutines.delay(1000)

                _uiState.value = ChangePasswordUiState(isSuccess = true)

            } catch (e: Exception) {
                _uiState.value = ChangePasswordUiState(
                    error = e.message ?: "Error desconocido al cambiar contraseña"
                )
            }
        }
    }


    private fun validateFields(
        contrasenaActual: String,
        nuevaContrasena: String,
        confirmarContrasena: String
    ): List<String> {
        val errors = mutableListOf<String>()

        if (contrasenaActual.isBlank()) {
            errors.add("Debes ingresar tu contraseña actual")
        }

        if (nuevaContrasena.length < ValidationConstants.MIN_PASSWORD_LENGTH) {
            errors.add("La nueva contraseña debe tener al menos ${ValidationConstants.MIN_PASSWORD_LENGTH} caracteres")
        }

        if (nuevaContrasena != confirmarContrasena) {
            errors.add("Las contraseñas no coinciden")
        }

        if (contrasenaActual.isNotBlank() && contrasenaActual == nuevaContrasena) {
            errors.add("La nueva contraseña debe ser diferente a la actual")
        }

        return errors
    }


    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null,
            validationErrors = emptyList()
        )
    }


    fun resetState() {
        _uiState.value = ChangePasswordUiState()
    }
}