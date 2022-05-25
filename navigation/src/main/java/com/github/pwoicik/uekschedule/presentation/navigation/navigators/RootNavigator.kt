package com.github.pwoicik.uekschedule.presentation.navigation.navigators

import androidx.navigation.NavHostController
import com.github.pwoicik.uekschedule.features.preferences.presentation.screens.preferences.PreferencesNavigator
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.singleGroupSchedulePreview.SingleGroupSchedulePreviewNavigator

class RootNavigator(
    private val rootNavController: NavHostController
) : PreferencesNavigator, SingleGroupSchedulePreviewNavigator {

    override fun navigateUp() {
        rootNavController.navigateUp()
    }
}
