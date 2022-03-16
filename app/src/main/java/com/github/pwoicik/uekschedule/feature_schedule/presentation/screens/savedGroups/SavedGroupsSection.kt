package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.savedGroups

import androidx.annotation.StringRes
import com.github.pwoicik.uekschedule.R

enum class SavedGroupsSection(@StringRes val title: Int) {
    SavedGroups(R.string.your_groups),
    OtherActivities(R.string.other_activities)
}
