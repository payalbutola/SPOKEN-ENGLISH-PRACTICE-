package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Playful Colorful Light Theme for students (Bento Grid Mod)
private val PlayfulLightColorScheme = lightColorScheme(
    primary = BentoPrimaryPurple,
    onPrimary = BentoOnPrimary,
    primaryContainer = BentoLightPurpleContainer,
    onPrimaryContainer = BentoDeepPurpleText,
    secondary = BentoSecondaryBlue,
    onSecondary = BentoOnPrimary,
    secondaryContainer = BentoBlueContainer,
    onSecondaryContainer = BentoDeepBlueText,
    tertiary = BentoTextColorDark,
    onTertiary = BentoOnPrimary,
    tertiaryContainer = BentoStreakPeach,
    onTertiaryContainer = BentoStreakMaroon,
    background = BentoBackgroundLight,
    surface = BentoSurfaceCloud,
    onBackground = BentoTextColorDark,
    onSurface = BentoTextColorDark,
    surfaceVariant = BentoInactiveLevelBg,
    onSurfaceVariant = BentoTextMuted,
    outline = BentoTextMuted
)

// Standard Dark Theme
private val PlayfulDarkColorScheme = darkColorScheme(
    primary = HighContrastCyan,
    onPrimary = CosmicNavy,
    primaryContainer = CosmicBlue,
    onPrimaryContainer = HighContrastYellow,
    secondary = HighContrastGreen,
    onSecondary = HighContrastBlack,
    tertiary = HighContrastYellow,
    background = CosmicNavy,
    surface = CosmicBlue,
    onBackground = MutedCloud,
    onSurface = MutedCloud
)

// Accessibility High-Contrast Mode Scheme (WCAG AAA)
val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastYellow,
    onPrimary = HighContrastBlack,
    primaryContainer = HighContrastBlack,
    onPrimaryContainer = HighContrastYellow,
    secondary = HighContrastCyan,
    onSecondary = HighContrastBlack,
    tertiary = HighContrastGreen,
    onTertiary = HighContrastBlack,
    background = HighContrastBlack,
    surface = HighContrastGray,
    onBackground = HighContrastWhite,
    onSurface = HighContrastWhite,
    error = HighContrastMagenta,
    onError = HighContrastWhite
)

fun ColorBackground(hex: Long) = androidx.compose.ui.graphics.Color(hex)

@Composable
fun EnglishShikshaTheme(
    highContrastMode: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    textSizeMultiplier: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrastMode -> HighContrastColorScheme
        darkTheme -> PlayfulDarkColorScheme
        else -> PlayfulLightColorScheme
    }

    // Build custom scaled typography based on multiplier
    val scaledTypography = androidx.compose.material3.Typography(
        displayLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = (57 * textSizeMultiplier).sp,
            lineHeight = (64 * textSizeMultiplier).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = (32 * textSizeMultiplier).sp,
            lineHeight = (40 * textSizeMultiplier).sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = (22 * textSizeMultiplier).sp,
            lineHeight = (28 * textSizeMultiplier).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * textSizeMultiplier).sp,
            lineHeight = (24 * textSizeMultiplier).sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * textSizeMultiplier).sp,
            lineHeight = (20 * textSizeMultiplier).sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * textSizeMultiplier).sp,
            lineHeight = (16 * textSizeMultiplier).sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}
