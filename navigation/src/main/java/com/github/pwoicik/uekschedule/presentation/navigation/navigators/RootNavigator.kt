package com.github.pwoicik.uekschedule.presentation.navigation.navigators

import androidx.navigation.NavHostController
import com.github.pwoicik.uekschedule.features.preferences.presentation.screens.preferences.PreferencesNavigator
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview.SchedulePreviewNavigator

class RootNavigator(
    private val rootNavController: NavHostController
) : PreferencesNavigator, SchedulePreviewNavigator {

    override fun navigateUp() {
        rootNavController.navigateUp()
    }
}
