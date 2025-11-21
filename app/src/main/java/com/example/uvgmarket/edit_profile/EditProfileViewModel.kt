package com.example.uvgmarket.edit_profile

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
import android.util.Log

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

    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "EditProfileViewModel"

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos del usuario actual desde Firebase
     */
    fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    Log.d(TAG, "Cargando datos del usuario: ${currentUser.uid}")

                    val user = userRepository.getUserById(currentUser.uid)

                    if (user != null) {
                        Log.d(TAG, "Datos cargados: ${user.nombre}, ${user.usuario}, ${user.correo}")
                        _uiState.value = EditProfileUiState(
                            nombre = user.nombre,
                            usuario = user.usuario,
                            correo = user.correo,
                            imagenPerfil = null, // Por ahora seguimos usando drawables hardcodeados
                            imagenPortada = null
                        )
                    } else {
                        Log.w(TAG, "No se encontraron datos del usuario")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "No se pudieron cargar los datos del usuario"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error cargando datos del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar datos: ${e.message}"
                )
            }
        }
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

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true)
                Log.d(TAG, "Guardando perfil...")

                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // Obtener usuario actual para mantener campos que no se editan
                    val existingUser = userRepository.getUserById(currentUser.uid)

                    if (existingUser != null) {
                        // Actualizar solo los campos editables
                        val updatedUser = existingUser.copy(
                            nombre = state.nombre,
                            usuario = state.usuario,
                            correo = state.correo
                        )

                        val success = userRepository.updateUser(updatedUser)

                        if (success) {
                            Log.d(TAG, "Perfil actualizado exitosamente")
                            _uiState.value = state.copy(
                                isLoading = false,
                                isSuccess = true
                            )
                        } else {
                            _uiState.value = state.copy(
                                isLoading = false,
                                error = "Error al guardar el perfil"
                            )
                        }
                    } else {
                        _uiState.value = state.copy(
                            isLoading = false,
                            error = "No se encontró el usuario"
                        )
                    }
                } else {
                    _uiState.value = state.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error guardando perfil", e)
                _uiState.value = state.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
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
        println("Cambiar imagen de perfil")
    }

    fun onCoverImageChange() {
        // TODO: Implementar lógica para seleccionar imagen
        println("Cambiar imagen de portada")
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = EditProfileUiState()
    }
}