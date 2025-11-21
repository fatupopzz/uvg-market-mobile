package com.example.uvgmarket.data.model

/**
 * Data class que representa un vendedor/emprendedor en la aplicación
 * Se sincroniza con Firebase Firestore
 */
data class Seller(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val imagenPerfil: String = "",      // Nombre del drawable o URL
    val imagenPortada: String = "",     // Nombre del drawable o URL
    val calificacion: Float = 0f,
    val totalVentas: Int = 0,
    val productImages: List<String> = emptyList(),  // Lista de imágenes de productos destacados
    val correo: String = "",
    val fechaCreacion: Long = System.currentTimeMillis(),
    val activo: Boolean = true
) {
    /**
     * Convierte el vendedor a un Map para guardarlo en Firestore
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "nombre" to nombre,
            "descripcion" to descripcion,
            "imagenPerfil" to imagenPerfil,
            "imagenPortada" to imagenPortada,
            "calificacion" to calificacion,
            "totalVentas" to totalVentas,
            "productImages" to productImages,
            "correo" to correo,
            "fechaCreacion" to fechaCreacion,
            "activo" to activo
        )
    }

    companion object {
        /**
         * Crea un vendedor desde un Map de Firestore
         */
        fun fromMap(map: Map<String, Any>): Seller {
            return Seller(
                id = map["id"] as? String ?: "",
                nombre = map["nombre"] as? String ?: "",
                descripcion = map["descripcion"] as? String ?: "",
                imagenPerfil = map["imagenPerfil"] as? String ?: "",
                imagenPortada = map["imagenPortada"] as? String ?: "",
                calificacion = (map["calificacion"] as? Number)?.toFloat() ?: 0f,
                totalVentas = (map["totalVentas"] as? Number)?.toInt() ?: 0,
                productImages = (map["productImages"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                correo = map["correo"] as? String ?: "",
                fechaCreacion = map["fechaCreacion"] as? Long ?: System.currentTimeMillis(),
                activo = map["activo"] as? Boolean ?: true
            )
        }
    }
}