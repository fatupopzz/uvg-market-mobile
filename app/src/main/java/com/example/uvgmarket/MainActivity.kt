package com.example.uvgmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uvgmarket.navigation.MainNavigation
import com.example.uvgmarket.ui.theme.UvgMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UvgMarketTheme {

                MainNavigation()
            }
        }
    }
}