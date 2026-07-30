package com.hermes.mobile.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = HermesBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE9F2FF),
    background = Color.White,
    surface = SurfaceLight,
    surfaceVariant = Color(0xFFE5E5EA),
    onBackground = Color(0xFF1D1D1F),
    onSurface = Color(0xFF1D1D1F),
    onSurfaceVariant = Color(0xFF86868B),
    outline = Color(0xFFC7C7CC),
    error = Color(0xFFFF3B30),
    secondary = Color(0xFF5856D6),
    tertiary = Color(0xFFAF52DE)
)

private val DarkColorScheme = darkColorScheme(
    primary = HermesBlueDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1A2A3A),
    background = Color.Black,
    surface = SurfaceDark,
    surfaceVariant = Color(0xFF2C2C2E),
    onBackground = Color(0xFFF5F5F7),
    onSurface = Color(0xFFF5F5F7),
    onSurfaceVariant = Color(0xFF98989D),
    outline = Color(0xFF636366),
    error = Color(0xFFFF453A),
    secondary = Color(0xFF5E5CE6),
    tertiary = Color(0xFFBF5AF2)
)

@Composable
fun HermesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = HermesTypography,
        shapes = HermesShapes,
        content = content
    )
}
