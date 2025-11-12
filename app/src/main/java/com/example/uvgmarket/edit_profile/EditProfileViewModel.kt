package com.example.uvgmarket.edit_profile



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvgmarket.core.constants.ValidationConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val nombre: String = "",
    val usuario: String = "",
    val correo: String = "",
    val imagenPerfil: Int? = null,
    val imagenPortada: Int? = null
)

class EditProfileViewModel : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(EditProfileUiState())

    // Estado público inmutable que observa la UI
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()


    fun initializeProfile(
        nombre: String,
        usuario: String,
        correo: String,
        imagenPerfil: Int,
        imagenPortada: Int
    ) {
        _uiState.value = EditProfileUiState(
            nombre = nombre,
            usuario = usuario,
            correo = correo,
            imagenPerfil = imagenPerfil,
            imagenPortada = imagenPortada
        )
    }


    fun updateNombre(nombre: String) {
        _uiState.value = _uiState.value.copy(
            nombre = nombre,
            error = null
        )
    }


    fun updateUsuario(usuario: String) {
        _uiState.value = _uiState.value.copy(
            usuario = usuario,
            error = null
        )
    }


    fun updateCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(
            correo = correo,
            error = null
        )
    }


    fun saveProfile() {
        val state = _uiState.value

        // Validar campos
        val validationError = validateFields(state.nombre, state.usuario, state.correo)
        if (validationError != null) {
            _uiState.value = state.copy(error = validationError)
            return
        }

        // Lanzar coroutine en el scope del ViewModel
        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true)

                // TODO: Implementar lógica con Firebase Firestore
                // Por ahora simulamos un delay
                kotlinx.coroutines.delay(1000)

                _uiState.value = state.copy(
                    isLoading = false,
                    isSuccess = true
                )

            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido al guardar perfil"
                )
            }
        }
    }


    private fun validateFields(
        nombre: String,
        usuario: String,
        correo: String
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
            else -> null
        }
    }


    fun onProfileImageChange() {
        // TODO: Implementar lógica para seleccionar imagen
        // Por ahora solo muestra un mensaje en los logs
        println("Cambiar imagen de perfil")
    }


    fun onCoverImageChange() {
        // TODO: Implementar lógica para seleccionar imagen
        // Por ahora solo muestra un mensaje en los logs
        println("Cambiar imagen de portada")
    }


    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


    fun resetState() {
        _uiState.value = EditProfileUiState()
    }
}