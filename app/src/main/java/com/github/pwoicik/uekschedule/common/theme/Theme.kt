package com.github.pwoicik.uekschedule.common.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences

private val LightThemeColors = lightColorScheme(

    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
)

private val DarkThemeColors = darkColorScheme(

    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
)

private val AmoledThemeColors = DarkThemeColors.copy(
    background = Color.Black,
    surface = Color.Black,
    inverseOnSurface = Color.Black
)

private fun dynamicAmoledColorScheme(context: Context) =
    dynamicDarkColorScheme(context).copy(
        background = Color.Black,
        surface = Color.Black,
        inverseOnSurface = Color.Black
    )

@Composable
fun UEKScheduleTheme(
    theme: Preferences.Theme,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colors = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        true -> {
            when (theme) {
                Preferences.Theme.AUTO ->
                    if (isSystemInDarkTheme()) DarkThemeColors
                    else LightThemeColors
                Preferences.Theme.LIGHT ->
                    LightThemeColors
                Preferences.Theme.DARK ->
                    DarkThemeColors
                Preferences.Theme.AMOLED ->
                    AmoledThemeColors
                Preferences.Theme.DYNAMIC_AUTO ->
                    if (isSystemInDarkTheme()) dynamicDarkColorScheme(context)
                    else dynamicLightColorScheme(context)
                Preferences.Theme.DYNAMIC_LIGHT ->
                    dynamicLightColorScheme(context)
                Preferences.Theme.DYNAMIC_DARK ->
                    dynamicDarkColorScheme(context)
                Preferences.Theme.DYNAMIC_AMOLED ->
                    dynamicAmoledColorScheme(context)
            }
        }
        false -> {
            when (theme) {
                Preferences.Theme.AUTO, Preferences.Theme.DYNAMIC_AUTO ->
                    if (isSystemInDarkTheme()) DarkThemeColors
                    else LightThemeColors
                Preferences.Theme.LIGHT, Preferences.Theme.DYNAMIC_LIGHT ->
                    LightThemeColors
                Preferences.Theme.DARK, Preferences.Theme.DYNAMIC_DARK ->
                    DarkThemeColors
                Preferences.Theme.AMOLED, Preferences.Theme.DYNAMIC_AMOLED ->
                    AmoledThemeColors
            }
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
