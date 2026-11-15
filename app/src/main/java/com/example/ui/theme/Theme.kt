package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SafarGoLightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = IndigoPrimaryContainer,
    onPrimaryContainer = IndigoOnPrimaryContainer,
    inversePrimary = IndigoInversePrimary,
    secondary = VioletSecondary,
    onSecondary = Color.White,
    secondaryContainer = VioletSecondaryContainer,
    onSecondaryContainer = Color(0xFFFFFBFF),
    tertiary = SkyTertiaryDeep,
    onTertiary = Color.White,
    tertiaryContainer = SkyTertiaryContainer,
    onTertiaryContainer = SkyOnTertiaryContainer,
    background = LightCanvas,
    onBackground = LightOnSurface,
    surface = LightSurfaceGlass,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceHigh,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerLowest = LightSurfaceGlassSelected,
    surfaceContainerLow = LightSurfaceLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceHigh,
    surfaceContainerHighest = LightSurfaceHighest,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    error = ErrorDeep,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer
)

private val SafarGoDarkColorScheme = darkColorScheme(
    primary = DarkPrimaryTint,
    onPrimary = IndigoDeep,
    primaryContainer = IndigoPrimaryContainer,
    onPrimaryContainer = IndigoFixed,
    inversePrimary = IndigoPrimary,
    secondary = VioletCta,
    onSecondary = Color.White,
    secondaryContainer = VioletOnFixedVariant,
    onSecondaryContainer = VioletFixed,
    tertiary = SkyBlueAccent,
    onTertiary = SkyOnFixed,
    tertiaryContainer = SkyTertiaryContainer,
    onTertiaryContainer = SkyFixed,
    background = DarkCanvasBase,
    onBackground = DarkTextPrimary,
    surface = DarkSurfaceGlass,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceContainerHigh,
    onSurfaceVariant = DarkTextSecondary,
    surfaceContainerLowest = DarkCanvasBase,
    surfaceContainerLow = DarkSurfaceGlassSubtle,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    outline = DarkTextSecondary,
    outlineVariant = DarkBorder,
    inverseSurface = LightSurfaceGlassSelected,
    inverseOnSurface = LightOnSurface,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xCC7F1D1D),
    onErrorContainer = Color(0xFFFECACA)
)

@Composable
fun SafarGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SafarGoDarkColorScheme else SafarGoLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SafarGoTypography,
        shapes = SafarGoShapes,
        content = content
    )
}
