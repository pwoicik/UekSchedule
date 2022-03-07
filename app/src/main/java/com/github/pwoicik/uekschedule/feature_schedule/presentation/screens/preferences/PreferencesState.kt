package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.preferences

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences

data class PreferencesState(

    val isDynamicTheme: Boolean = false,
    val theme: Preferences.Theme = Preferences.Defaults.THEME
)
