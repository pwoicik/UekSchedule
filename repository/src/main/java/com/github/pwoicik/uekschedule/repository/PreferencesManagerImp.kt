package com.github.pwoicik.uekschedule.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.pwoicik.uekschedule.common.domain.PreferencesManager
import com.github.pwoicik.uekschedule.domain.model.Preferences
import kotlinx.coroutines.flow.map

class PreferencesManagerImpl(
    context: Context
) : PreferencesManager {

    private val preferences = preferencesDataStore("preferences")
        .getValue(context, context::javaClass)

    override suspend fun setLastUsedAppVersion(versionCode: Int) {
        preferences.edit { preferences ->
            preferences[Keys.LAST_USED_APP_VERSION] = versionCode
        }
    }

    override val lastUsedAppVersion = preferences.data.map { preferences ->
        preferences[Keys.LAST_USED_APP_VERSION]
    }

    override suspend fun setTheme(theme: Preferences.Theme) {
        preferences.edit { preferences ->
            preferences[Keys.THEME] = theme.ordinal
        }
    }

    override val theme = preferences.data.map { preferences ->
        val themeInt = preferences[Keys.THEME]
        if (themeInt == null) Preferences.Defaults.THEME
        else Preferences.Theme.entries[themeInt]
    }

    private object Keys {
        val LAST_USED_APP_VERSION = intPreferencesKey("LAST_USED_APP_VERSION")
        val THEME = intPreferencesKey("THEME")
    }
}
