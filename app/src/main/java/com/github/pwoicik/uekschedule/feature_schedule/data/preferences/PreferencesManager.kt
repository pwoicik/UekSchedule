package com.github.pwoicik.uekschedule.feature_schedule.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Preferences
import kotlinx.coroutines.flow.map

private val Context.preferences by preferencesDataStore("preferences")

class PreferencesManager(context: Context) {

    private val preferences = context.preferences

    suspend fun setTheme(theme: Preferences.Theme) {
        preferences.edit { preferences ->
            preferences[Keys.THEME] = theme.ordinal
        }
    }

    val theme = preferences.data.map { preferences ->
        val themeInt = preferences[Keys.THEME]
        if (themeInt == null) Preferences.Defaults.THEME
        else Preferences.Theme.values()[themeInt]
    }

    private object Keys {
        val THEME = intPreferencesKey("THEME")
    }
}
