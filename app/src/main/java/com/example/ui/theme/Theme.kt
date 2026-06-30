package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = AgroGold,
    secondary = AgroGreenLight,
    tertiary = AgroTerracotta,
    background = AgroDarkBackground,
    surface = AgroDarkSurface,
    onPrimary = AgroGreenDark,
    onSecondary = AgroGreenDark,
    onTertiary = AgroDarkBackground,
    onBackground = AgroBackground,
    onSurface = AgroBackground
  )

private val LightColorScheme =
  lightColorScheme(
    primary = AgroGreen,
    secondary = AgroGold,
    tertiary = AgroTerracotta,
    background = AgroBackground,
    surface = AgroLightSurface,
    onPrimary = AgroLightSurface,
    onSecondary = AgroGreenDark,
    onTertiary = AgroLightSurface,
    onBackground = AgroGreenDark,
    onSurface = AgroGreenDark
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disabling dynamicColor by default to maintain our beautiful custom brand identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
