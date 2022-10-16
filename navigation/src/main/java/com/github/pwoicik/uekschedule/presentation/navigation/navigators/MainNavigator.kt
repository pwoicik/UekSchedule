package com.github.pwoicik.uekschedule.presentation.navigation.navigators

import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.createActivity.CreateActivityNavigator
import com.github.pwoicik.uekschedule.features.preferences.presentation.screens.preferences.destinations.PreferencesScreenDestination
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.SchedulePreviewScreenDestination
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule.ScheduleNavigator
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.SearchNavigator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainNavigator(
    private val mainNavController: DestinationsNavigator,
    private val rootNavController: DestinationsNavigator
) : SearchNavigator, CreateActivityNavigator, ScheduleNavigator {
    override fun navigateUp() {
        mainNavController.navigateUp()
    }

    override fun openPreferences() {
        rootNavController.navigate(PreferencesScreenDestination)
    }

    override fun openSchedulePreview(schedulable: Schedulable) {
        rootNavController.navigate(
            SchedulePreviewScreenDestination(
                schedulable.id,
                schedulable.name,
                schedulable.type
            )
        )
    }
}
