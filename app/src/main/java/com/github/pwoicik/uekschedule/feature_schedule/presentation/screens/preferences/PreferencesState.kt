package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.preferences

import androidx.annotation.StringRes
import com.github.pwoicik.uekschedule.R
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences

data class PreferencesState(

    val isDynamicTheme: Boolean = false,
    val theme: ThemePreference = ThemePreference.fromPreferencesTheme(Preferences.Defaults.THEME)
)

enum class ThemePreference(@StringRes val stringRes: Int) {
    AUTO(R.string.theme_auto),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
    AMOLED(R.string.theme_amoled),
    ;

    fun toPreferencesTheme(isDynamic: Boolean) = when (isDynamic) {
        true -> when (this) {
            AUTO -> Preferences.Theme.DYNAMIC_AUTO
            LIGHT -> Preferences.Theme.DYNAMIC_LIGHT
            DARK -> Preferences.Theme.DYNAMIC_DARK
            AMOLED -> Preferences.Theme.DYNAMIC_AMOLED
        }
        false -> when (this) {
            AUTO -> Preferences.Theme.AUTO
            LIGHT -> Preferences.Theme.LIGHT
            DARK -> Preferences.Theme.DARK
            AMOLED -> Preferences.Theme.AMOLED
        }
    }

    companion object {
        fun fromPreferencesTheme(theme: Preferences.Theme) = when (theme) {
            Preferences.Theme.AUTO,
            Preferences.Theme.DYNAMIC_AUTO -> AUTO
            Preferences.Theme.LIGHT,
            Preferences.Theme.DYNAMIC_LIGHT -> LIGHT
            Preferences.Theme.DARK,
            Preferences.Theme.DYNAMIC_DARK -> DARK
            Preferences.Theme.AMOLED,
            Preferences.Theme.DYNAMIC_AMOLED -> AMOLED
        }
    }
}
