package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StarVibeColorScheme = darkColorScheme(
    primary = StarVibeTurquoise,
    onPrimary = Color(0xFF003833),
    primaryContainer = StarVibeTealDeep,
    onPrimaryContainer = StarVibeEmerald,
    secondary = StarVibeGold,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF4E342E),
    onSecondaryContainer = StarVibeGoldLight,
    tertiary = StarVibeEmerald,
    onTertiary = Color(0xFF003820),
    background = StarVibeBgDark,
    onBackground = StarVibeTextPrimary,
    surface = StarVibeSurfaceDark,
    onSurface = StarVibeTextPrimary,
    surfaceVariant = StarVibeSurfaceCard,
    onSurfaceVariant = StarVibeTextSecondary,
    outline = StarVibeGoldBorder,
    outlineVariant = Color(0xFF134E4A)
)

@Composable
fun StarVibeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = StarVibeColorScheme,
        typography = Typography,
        content = content
    )
}
