package com.fjjukic.zenvio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = OnPrimaryDark,

    secondary = Secondary,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = OnSecondaryDark,

    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    surface = SurfaceLight,
    onSurface = OnBackgroundLight,

    error = Error,
    onError = OnError,
    errorContainer = ErrorDark,
    onErrorContainer = OnErrorDark,

    outline = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = OnPrimaryLight,

    secondary = SecondaryLight,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = OnSecondaryLight,

    background = BackgroundDark,
    onBackground = OnBackgroundDark,

    surface = SurfaceDark,
    onSurface = OnBackgroundDark,

    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = Error,
    onErrorContainer = OnError,

    outline = OutlineDark
)

@Composable
fun ZenvioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}