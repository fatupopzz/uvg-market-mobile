package com.example.uvgmarket.data.model

/**
 * Data class que representa un usuario en la aplicación
 * Se sincroniza con Firebase Firestore
 */
data class User(
    val uid: String = "",              // ID único de Firebase Auth
    val nombre: String = "",
    val usuario: String = "",          // Username único
    val correo: String = "",
    val imagenPerfil: String = "",     // URL de la imagen de perfil
    val imagenPortada: String = "",    // URL de la imagen de portada
    val fechaCreacion: Long = System.currentTimeMillis(),
    val rating: Int = 0,               // Rating del vendedor (0-5)
    val totalVentas: Int = 0           // Total de ventas realizadas
) {
    /**
     * Convierte el usuario a un Map para guardarlo en Firestore
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "uid" to uid,
            "nombre" to nombre,
            "usuario" to usuario,
            "correo" to correo,
            "imagenPerfil" to imagenPerfil,
            "imagenPortada" to imagenPortada,
            "fechaCreacion" to fechaCreacion,
            "rating" to rating,
            "totalVentas" to totalVentas
        )
    }

    companion object {
        /**
         * Crea un usuario desde un Map de Firestore
         */
        fun fromMap(map: Map<String, Any>): User {
            return User(
                uid = map["uid"] as? String ?: "",
                nombre = map["nombre"] as? String ?: "",
                usuario = map["usuario"] as? String ?: "",
                correo = map["correo"] as? String ?: "",
                imagenPerfil = map["imagenPerfil"] as? String ?: "",
                imagenPortada = map["imagenPortada"] as? String ?: "",
                fechaCreacion = map["fechaCreacion"] as? Long ?: System.currentTimeMillis(),
                rating = (map["rating"] as? Long)?.toInt() ?: 0,
                totalVentas = (map["totalVentas"] as? Long)?.toInt() ?: 0
            )
        }
    }
}