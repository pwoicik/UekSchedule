package com.github.pwoicik.uekschedule.presentation.navigation.screens.yourGroups

import androidx.annotation.StringRes
import com.github.pwoicik.uekschedule.common.R
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.destinations.OtherActivitiesScreenDestination
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups.destinations.SavedGroupsScreenDestination
import com.ramcosta.composedestinations.spec.Direction

internal enum class YourGroupsDestination(
    val direction: Direction,
    @StringRes val label: Int
) {

    SavedGroupsScreen(SavedGroupsScreenDestination, R.string.your_groups),
    OtherActivitiesScreen(OtherActivitiesScreenDestination, R.string.other_activities)
}
