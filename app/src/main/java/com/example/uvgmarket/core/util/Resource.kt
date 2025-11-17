package com.example.uvgmarket.core.util

/**
 * Sealed class para representar el estado de una operación
 * Útil para manejar loading, success y error en ViewModels
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Estado de éxito - La operación se completó exitosamente
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Estado de error - La operación falló
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Estado de carga - La operación está en progreso
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}