package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val GamesClubDarkColorScheme = darkColorScheme(
    primary = FieryRed,
    onPrimary = TextPrimary,
    primaryContainer = SlateCardBg,
    onPrimaryContainer = TextPrimary,
    secondary = FlameOrange,
    onSecondary = TextPrimary,
    tertiary = GoldYellow,
    onTertiary = SlateDarkBg,
    background = SlateDarkBg,
    onBackground = TextPrimary,
    surface = SlateCardBg,
    onSurface = TextPrimary,
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = SlateCardBorder
)

@Composable
fun GamesClubBDTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SlateDarkBg.toArgb()
            window.navigationBarColor = SlateDarkBg.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = GamesClubDarkColorScheme,
        typography = Typography,
        content = content
    )
}
