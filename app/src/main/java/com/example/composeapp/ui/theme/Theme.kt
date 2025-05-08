package com.example.composeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RecipesAppLightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = AccentColor,
    tertiary = AccentBlue,
    tertiaryContainer = SliderTrackColor,
    onPrimary = TextPrimaryColor,
    onSecondary = TextSecondaryColor,
    outlineVariant = DividerColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    surfaceVariant = SurfaceVariantColor,
)

private val RecipesAppDarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    secondary = AccentColorDark,
    tertiary = AccentBlueDark,
    tertiaryContainer = SliderTrackColorDark,
    onPrimary = TextPrimaryColorDark,
    onSecondary = TextSecondaryColorDark,
    background = BackgroundColorDark,
    surface = SurfaceColorDark,
)

@Composable
fun RecipesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RecipesAppDarkColorScheme else RecipesAppLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
