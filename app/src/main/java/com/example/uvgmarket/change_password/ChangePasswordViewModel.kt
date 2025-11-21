package com.example.uvgmarket.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.core.constants.ValidationConstants
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val validationErrors: List<String> = emptyList()
)

class ChangePasswordViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "ChangePasswordViewModel"

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
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

        viewModelScope.launch {
            try {
                _uiState.value = ChangePasswordUiState(isLoading = true)
                Log.d(TAG, "=== INICIANDO CAMBIO DE CONTRASEÑA ===")

                val user = auth.currentUser
                if (user == null || user.email == null) {
                    _uiState.value = ChangePasswordUiState(
                        error = "No hay usuario autenticado"
                    )
                    return@launch
                }

                Log.d(TAG, "Usuario autenticado: ${user.email}")

                // Paso 1: Re-autenticar al usuario con la contraseña actual
                Log.d(TAG, "Re-autenticando usuario...")
                val credential = EmailAuthProvider.getCredential(user.email!!, contrasenaActual)

                try {
                    user.reauthenticate(credential).await()
                    Log.d(TAG, "✓ Re-autenticación exitosa")
                } catch (e: Exception) {
                    Log.e(TAG, "✗ Error en re-autenticación", e)
                    _uiState.value = ChangePasswordUiState(
                        error = "La contraseña actual es incorrecta"
                    )
                    return@launch
                }

                // Paso 2: Cambiar la contraseña
                Log.d(TAG, "Cambiando contraseña...")
                try {
                    user.updatePassword(nuevaContrasena).await()
                    Log.d(TAG, "✓ Contraseña cambiada exitosamente")

                    _uiState.value = ChangePasswordUiState(isSuccess = true)
                } catch (e: Exception) {
                    Log.e(TAG, "✗ Error al cambiar contraseña", e)

                    val errorMessage = when {
                        e.message?.contains("weak-password") == true ->
                            "La nueva contraseña es muy débil"
                        e.message?.contains("requires-recent-login") == true ->
                            "Debes volver a iniciar sesión para cambiar tu contraseña"
                        else -> "Error al cambiar la contraseña: ${e.message}"
                    }

                    _uiState.value = ChangePasswordUiState(error = errorMessage)
                }

            } catch (e: Exception) {
                Log.e(TAG, "=== ERROR GENERAL ===", e)
                _uiState.value = ChangePasswordUiState(
                    error = "Error inesperado: ${e.message}"
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