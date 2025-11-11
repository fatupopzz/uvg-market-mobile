package com.example.uvgmarket.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = UvgGreen,                    // Color principal
    onPrimary = TextWhite,                 // Texto sobre primary
    primaryContainer = UvgGreenLight,      // Contenedores de primary
    onPrimaryContainer = UvgGreen,         // Texto sobre primaryContainer

    // Colores secundarios
    secondary = UvgGreenDark,              // Color secundario
    onSecondary = TextWhite,               // Texto sobre secondary
    secondaryContainer = UvgGreenLight,    // Contenedores secundarios
    onSecondaryContainer = UvgGreen,       // Texto sobre secondaryContainer

    // Colores terciarios
    tertiary = UvgGreenLight,              // Color terciario
    onTertiary = TextWhite,                // Texto sobre tertiary
    tertiaryContainer = SurfaceGray,       // Contenedores terciarios
    onTertiaryContainer = TextDark,        // Texto sobre tertiaryContainer

    // Colores de error
    error = ErrorRed,                      // Color de error
    onError = TextWhite,                   // Texto sobre error
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,

    // Fondos y superficies
    background = BackgroundWhite,          // Fondo de la app
    onBackground = TextDark,               // Texto sobre background
    surface = SurfaceWhite,                // Superficies
    onSurface = TextDark,                  // Texto sobre surface
    surfaceVariant = SurfaceGray,          // Variante de superficie
    onSurfaceVariant = TextGray,           // Texto sobre surfaceVariant

    // Extras
    outline = IconGray,                    // Íconos grises
    outlineVariant = StarInactive,         // Variante de outline

)

@Composable
fun UvgMarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}