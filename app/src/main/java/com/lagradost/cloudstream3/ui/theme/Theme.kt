package com.lagradost.cloudstream3.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = KINO_Red,
    secondary = KINO_Purple,
    tertiary = KINO_Gold,
    background = Background,
    surface = Surface,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = Background,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun KINOTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
