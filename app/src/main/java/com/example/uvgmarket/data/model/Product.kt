package com.example.uvgmarket.data.model

/**
 * Data class que representa un producto en la aplicación
 * Se sincroniza con Firebase Firestore
 */
data class Product(
    val id: String = "",
    val nombre: String = "",
    val subtitulo: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val imagen: String = "",           // Nombre del drawable o URL de imagen
    val vendedorId: String = "",
    val vendedorNombre: String = "",
    val fechaCreacion: Long = System.currentTimeMillis(),
    val activo: Boolean = true
) {
    /**
     * Convierte el producto a un Map para guardarlo en Firestore
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "nombre" to nombre,
            "subtitulo" to subtitulo,
            "descripcion" to descripcion,
            "precio" to precio,
            "imagen" to imagen,
            "vendedorId" to vendedorId,
            "vendedorNombre" to vendedorNombre,
            "fechaCreacion" to fechaCreacion,
            "activo" to activo
        )
    }

    companion object {
        /**
         * Crea un producto desde un Map de Firestore
         */
        fun fromMap(map: Map<String, Any>): Product {
            return Product(
                id = map["id"] as? String ?: "",
                nombre = map["nombre"] as? String ?: "",
                subtitulo = map["subtitulo"] as? String ?: "",
                descripcion = map["descripcion"] as? String ?: "",
                precio = (map["precio"] as? Number)?.toDouble() ?: 0.0,
                imagen = map["imagen"] as? String ?: "",
                vendedorId = map["vendedorId"] as? String ?: "",
                vendedorNombre = map["vendedorNombre"] as? String ?: "",
                fechaCreacion = map["fechaCreacion"] as? Long ?: System.currentTimeMillis(),
                activo = map["activo"] as? Boolean ?: true
            )
        }
    }
}