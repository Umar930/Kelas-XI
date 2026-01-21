package com.umar.warunggo.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Dark color scheme optimized for fintech/UMKM app
 * High contrast for accessibility
 */
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color(0xFF003258),
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = Color(0xFFD1E4FF),
    
    secondary = SecondaryTealLight,
    onSecondary = Color(0xFF003640),
    secondaryContainer = SecondaryTealDark,
    onSecondaryContainer = Color(0xFFB2EBF2),
    
    tertiary = TertiaryAmber,
    onTertiary = Color(0xFF3E2D00),
    tertiaryContainer = TertiaryAmberDark,
    onTertiaryContainer = Color(0xFFFFECB3),
    
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = DarkBackground,
    onBackground = Color(0xFFE6E6E6),
    surface = DarkSurface,
    onSurface = Color(0xFFE6E6E6),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC4C7C5),
    
    outline = Color(0xFF8E918F),
    outlineVariant = Color(0xFF44474E)
)

/**
 * Light color scheme (fallback)
 */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = Color(0xFF001D36),
    
    secondary = SecondaryTeal,
    onSecondary = Color.White,
    secondaryContainer = SecondaryTealLight,
    onSecondaryContainer = Color(0xFF001F24),
    
    tertiary = TertiaryAmberDark,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryAmber,
    onTertiaryContainer = Color(0xFF2E1500),
    
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    background = LightBackground,
    onBackground = Color(0xFF1A1C1E),
    surface = LightSurface,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE3EB),
    onSurfaceVariant = Color(0xFF42474E),
    
    outline = Color(0xFF73777F),
    outlineVariant = Color(0xFFC3C7CF)
)

@Composable
fun WarungGoTheme(
    darkTheme: Boolean = true, // Default to dark theme
    dynamicColor: Boolean = false, // Disable dynamic color for consistent branding
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
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}