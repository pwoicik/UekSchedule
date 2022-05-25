package com.github.pwoicik.uekschedule.common.domain

import com.github.pwoicik.uekschedule.domain.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    val lastUsedAppVersion: Flow<Int?>
    suspend fun setLastUsedAppVersion(versionCode: Int)

    val theme: Flow<Preferences.Theme>
    suspend fun setTheme(theme: Preferences.Theme)
}
