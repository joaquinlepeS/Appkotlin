package com.example.logincompose.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.app_kotlin.ui.theme.AppTypography
import com.example.app_kotlin.ui.theme.backgroundDark
import com.example.app_kotlin.ui.theme.backgroundLight
import com.example.app_kotlin.ui.theme.errorContainerDark
import com.example.app_kotlin.ui.theme.errorContainerLight
import com.example.app_kotlin.ui.theme.errorDark
import com.example.app_kotlin.ui.theme.errorLight
import com.example.app_kotlin.ui.theme.inverseOnSurfaceDark
import com.example.app_kotlin.ui.theme.inverseOnSurfaceLight
import com.example.app_kotlin.ui.theme.inversePrimaryDark
import com.example.app_kotlin.ui.theme.inversePrimaryLight
import com.example.app_kotlin.ui.theme.inverseSurfaceDark
import com.example.app_kotlin.ui.theme.inverseSurfaceLight
import com.example.app_kotlin.ui.theme.onBackgroundDark
import com.example.app_kotlin.ui.theme.onBackgroundLight
import com.example.app_kotlin.ui.theme.onErrorContainerDark
import com.example.app_kotlin.ui.theme.onErrorContainerLight
import com.example.app_kotlin.ui.theme.onErrorDark
import com.example.app_kotlin.ui.theme.onErrorLight
import com.example.app_kotlin.ui.theme.onPrimaryContainerDark
import com.example.app_kotlin.ui.theme.onPrimaryContainerLight
import com.example.app_kotlin.ui.theme.onPrimaryDark
import com.example.app_kotlin.ui.theme.onPrimaryLight
import com.example.app_kotlin.ui.theme.onSecondaryContainerDark
import com.example.app_kotlin.ui.theme.onSecondaryContainerLight
import com.example.app_kotlin.ui.theme.onSecondaryDark
import com.example.app_kotlin.ui.theme.onSecondaryLight
import com.example.app_kotlin.ui.theme.onSurfaceDark
import com.example.app_kotlin.ui.theme.onSurfaceLight
import com.example.app_kotlin.ui.theme.onSurfaceVariantDark
import com.example.app_kotlin.ui.theme.onSurfaceVariantLight
import com.example.app_kotlin.ui.theme.onTertiaryContainerDark
import com.example.app_kotlin.ui.theme.onTertiaryContainerLight
import com.example.app_kotlin.ui.theme.onTertiaryDark
import com.example.app_kotlin.ui.theme.onTertiaryLight
import com.example.app_kotlin.ui.theme.outlineDark
import com.example.app_kotlin.ui.theme.outlineLight
import com.example.app_kotlin.ui.theme.primaryContainerDark
import com.example.app_kotlin.ui.theme.primaryContainerLight
import com.example.app_kotlin.ui.theme.primaryDark
import com.example.app_kotlin.ui.theme.primaryLight
import com.example.app_kotlin.ui.theme.secondaryContainerDark
import com.example.app_kotlin.ui.theme.secondaryContainerLight
import com.example.app_kotlin.ui.theme.secondaryDark
import com.example.app_kotlin.ui.theme.secondaryLight
import com.example.app_kotlin.ui.theme.surfaceDark
import com.example.app_kotlin.ui.theme.surfaceLight
import com.example.app_kotlin.ui.theme.surfaceVariantDark
import com.example.app_kotlin.ui.theme.surfaceVariantLight
import com.example.app_kotlin.ui.theme.tertiaryContainerDark
import com.example.app_kotlin.ui.theme.tertiaryContainerLight
import com.example.app_kotlin.ui.theme.tertiaryDark
import com.example.app_kotlin.ui.theme.tertiaryLight

// 🌞 Paleta clara
private val LightColors = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
)

private val DarkColors = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
)

@Composable
fun LoginComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // 🔄 Cambia según el modo del sistema
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (LocalContext.current as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

