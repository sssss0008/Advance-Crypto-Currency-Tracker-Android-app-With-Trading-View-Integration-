package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CryptoPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = CryptoDarkSurfaceVariant,
    onPrimaryContainer = CryptoLightText,
    secondary = CryptoAccentGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0x33F7931A),
    onSecondaryContainer = CryptoAccentGold,
    tertiary = CryptoAccentCyan,
    background = CryptoDarkBackground,
    onBackground = CryptoLightText,
    surface = CryptoDarkSurface,
    onSurface = CryptoLightText,
    surfaceVariant = CryptoDarkSurfaceVariant,
    onSurfaceVariant = CryptoNeutralText,
    outline = CryptoDarkBorder,
    outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
    primary = CryptoPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0EDFF),
    onPrimaryContainer = CryptoPrimary,
    secondary = CryptoAccentGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFFB45309),
    tertiary = CryptoAccentCyan,
    background = CryptoLightBackground,
    onBackground = Color(0xFF0F172A),
    surface = CryptoLightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = CryptoLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF64748B),
    outline = CryptoLightBorder,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek crypto dark theme
    dynamicColor: Boolean = false, // Keep consistent crypto financial branding
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
