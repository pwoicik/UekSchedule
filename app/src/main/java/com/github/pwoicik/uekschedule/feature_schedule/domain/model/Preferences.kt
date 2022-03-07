package com.github.pwoicik.uekschedule.feature_schedule.domain.model

import androidx.annotation.StringRes
import androidx.datastore.preferences.core.intPreferencesKey
import com.github.pwoicik.uekschedule.R

object Preferences {
    val THEME = intPreferencesKey("THEME")
    enum class Theme(@StringRes val stringRes: Int) {
        // DON'T CHANGE THE ORDER
        // Theme is being saved as it's ordinal in DataStore
        AUTO(R.string.theme_auto),
        LIGHT(R.string.theme_light),
        DARK(R.string.theme_dark),
        AMOLED(R.string.theme_amoled),
        DYNAMIC_AUTO(R.string.theme_auto),
        DYNAMIC_LIGHT(R.string.theme_light),
        DYNAMIC_DARK(R.string.theme_dark),
        DYNAMIC_AMOLED(R.string.theme_amoled),
        ;

        val isDynamic = ordinal >= 4

        companion object {
            fun values(dynamic: Boolean): List<Theme> {
                return if (dynamic) {
                    values().filter(Theme::isDynamic)
                } else {
                    values().filter { !it.isDynamic }
                }
            }
        }
    }


    object Defaults {
        val THEME = Theme.AUTO
        val DYNAMIC_THEME = Theme.DYNAMIC_AUTO
    }
}
