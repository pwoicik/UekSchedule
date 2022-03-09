package com.github.pwoicik.uekschedule.feature_schedule.domain.model

object Preferences {
    enum class Theme {
        // DON'T CHANGE THE ORDER
        // Theme is being saved as it's ordinal in DataStore
        AUTO,           // 0
        LIGHT,          // 1
        DARK,           // 2
        AMOLED,         // 3
        DYNAMIC_AUTO,   // 4
        DYNAMIC_LIGHT,  // 5
        DYNAMIC_DARK,   // 6
        DYNAMIC_AMOLED, // 7
        ;

        val isDynamic = ordinal >= 4
    }

    object Defaults {
        val THEME = Theme.AUTO
    }
}
