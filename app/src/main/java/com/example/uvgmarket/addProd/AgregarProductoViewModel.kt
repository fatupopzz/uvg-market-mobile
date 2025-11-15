package com.example.uvgmarket.addProd

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel para la pantalla de agregar producto
 * Maneja el estado y la lógica de negocio
 */
class AgregarProductoViewModel : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(AgregarProductoUiState())

    // Estado público inmutable para la UI
    val uiState: StateFlow<AgregarProductoUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el nombre del producto
     */
    fun onNombreChange(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre) }
    }

    /**
     * Actualiza la descripción del producto
     */
    fun onDescripcionChange(nuevaDescripcion: String) {
        _uiState.update { it.copy(descripcion = nuevaDescripcion) }
    }

    /**
     * Actualiza el precio del producto
     */
    fun onPrecioChange(nuevoPrecio: String) {
        _uiState.update { it.copy(precio = nuevoPrecio) }
    }

    /**
     * Alterna el estado de imagen seleccionada
     */
    fun onImagenClick() {
        _uiState.update { it.copy(imagenSeleccionada = !it.imagenSeleccionada) }
    }

    /**
     * Valida si el formulario está completo para publicar
     */
    fun puedePublicar(): Boolean {
        val state = _uiState.value
        return state.nombre.isNotBlank() &&
                state.descripcion.isNotBlank() &&
                state.precio.isNotBlank()
    }

    /**
     * Limpia todos los campos del formulario
     */
    fun limpiarFormulario() {
        _uiState.value = AgregarProductoUiState()
    }
}

/**
 * Estado de la UI para agregar producto
 */
data class AgregarProductoUiState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val imagenSeleccionada: Boolean = false
)