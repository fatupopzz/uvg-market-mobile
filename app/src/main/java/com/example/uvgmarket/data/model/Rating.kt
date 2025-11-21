// app/src/main/java/com/example/uvgmarket/data/model/Rating.kt
package com.example.uvgmarket.data.model

/**
 * Data class que representa una calificación de un usuario a otro
 */
data class Rating(
    val id: String = "",
    val fromUserId: String = "",      // Quien califica
    val toUserId: String = "",        // A quien se califica
    val rating: Int = 0,              // Calificación (1-5)
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "fromUserId" to fromUserId,
            "toUserId" to toUserId,
            "rating" to rating,
            "timestamp" to timestamp
        )
    }

    companion object {
        fun fromMap(id: String, map: Map<String, Any>): Rating {
            return Rating(
                id = id,
                fromUserId = map["fromUserId"] as? String ?: "",
                toUserId = map["toUserId"] as? String ?: "",
                rating = (map["rating"] as? Long)?.toInt() ?: 0,
                timestamp = map["timestamp"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}