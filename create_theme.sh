#!/bin/bash
mkdir -p app/src/main/java/com/lagradost/cloudstream3/ui/theme

# Color.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/theme/Color.kt
package com.lagradost.cloudstream3.ui.theme

import androidx.compose.ui.graphics.Color

val Background = Color(0xFF080808)
val Surface = Color(0xFF0F0F0F)
val CardBackground = Color(0xFF141414)
val KINO_Red = Color(0xFFE50914)
val KINO_Purple = Color(0xFF7B2FBE)
val KINO_Gold = Color(0xFFF5C518)
val TextPrimary = Color(0xFFF5F5F5)
val TextMuted = Color(0xFF8A8A8A)
val TextDarkGray = Color(0xFF444444)
EOF

# Type.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/theme/Type.kt
package com.lagradost.cloudstream3.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
EOF

# Theme.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/theme/Theme.kt
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
EOF
