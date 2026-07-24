package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LeicaRed,
    onPrimary = Color.White,
    primaryContainer = LeicaRedDark,
    onPrimaryContainer = Color.White,
    secondary = CameraAccentGold,
    onSecondary = CameraObsidian,
    tertiary = FocusPeakingGreen,
    background = CameraObsidian,
    onBackground = CameraTextPrimary,
    surface = CameraSurfaceDark,
    onSurface = CameraTextPrimary,
    surfaceVariant = CameraSurfaceBorder,
    onSurfaceVariant = CameraTextSecondary
)

@Composable
fun TeleCamTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
