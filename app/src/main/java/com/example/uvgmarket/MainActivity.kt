package com.example.uvgmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uvgmarket.core.constants.navigation.AppNavigationHost
import com.example.uvgmarket.data.remote.FirebaseConfig
import com.example.uvgmarket.ui.theme.UvgMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase UNA SOLA VEZ
        FirebaseConfig.initialize()

        enableEdgeToEdge()
        setContent {
            UvgMarketTheme {
                AppNavigationHost()
            }
        }
    }
}