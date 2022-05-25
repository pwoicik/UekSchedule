package com.github.pwoicik.uekschedule.presentation.navigation.navigators

import com.github.pwoicik.uekschedule.features.activities.presentation.screens.createActivity.CreateActivityNavigator
import com.github.pwoicik.uekschedule.features.preferences.presentation.screens.preferences.destinations.PreferencesScreenDestination
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.SingleGroupSchedulePreviewScreenDestination
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule.ScheduleNavigator
import com.github.pwoicik.uekschedule.features.search.presentation.screens.allGroups.AllGroupsNavigator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainNavigator(
    private val mainNavController: DestinationsNavigator,
    private val rootNavController: DestinationsNavigator
) : AllGroupsNavigator, CreateActivityNavigator, ScheduleNavigator {
    override fun navigateUp() {
        mainNavController.navigateUp()
    }

    override fun openPreferences() {
        rootNavController.navigate(PreferencesScreenDestination)
    }

    override fun openSingleGroupSchedulePreview(groupId: Long, groupName: String) {
        rootNavController.navigate(SingleGroupSchedulePreviewScreenDestination(groupId, groupName))
    }
}
