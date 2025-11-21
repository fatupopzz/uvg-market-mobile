package com.example.uvgmarket.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

/**
 * Configuración optimizada de Firebase
 */
object FirebaseConfig {

    private val TAG = "FirebaseConfig"
    private var isInitialized = false

    /**
     * Inicializa Firebase con configuración optimizada
     * Llamar esto SOLO UNA VEZ al iniciar la app
     */
    fun initialize() {
        if (isInitialized) {
            Log.w(TAG, "Firebase ya está inicializado")
            return
        }

        try {
            val firestore = FirebaseFirestore.getInstance()

            // Configurar settings optimizados
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Habilitar caché offline
                .build()

            firestore.firestoreSettings = settings

            Log.d(TAG, "✓ Firebase configurado exitosamente con caché habilitada")
            isInitialized = true

        } catch (e: Exception) {
            Log.e(TAG, "Error configurando Firebase", e)
        }
    }

    /**
     * Obtiene instancia de Firestore configurada
     */
    fun getFirestore(): FirebaseFirestore {
        if (!isInitialized) {
            initialize()
        }
        return FirebaseFirestore.getInstance()
    }
}

