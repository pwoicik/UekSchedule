package com.github.pwoicik.uekschedule.presentation.navigation

import com.github.pwoicik.uekschedule.features.activities.presentation.screens.destinations.CreateActivityScreenDestination
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.destinations.OtherActivitiesScreenDestination
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects.destinations.GroupSubjectsScreenDestination
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups.destinations.SavedGroupsScreenDestination
import com.github.pwoicik.uekschedule.features.preferences.presentation.screens.preferences.destinations.PreferencesScreenDestination
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.SchedulePreviewScreenDestination
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.ScheduleScreenDestination
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.destinations.SearchScreenDestination
import com.github.pwoicik.uekschedule.presentation.navigation.screens.destinations.MainScreenDestination
import com.github.pwoicik.uekschedule.presentation.navigation.screens.destinations.YourGroupsScreenDestination

internal object NavGraphs {

    val yourGroups = NavGraph(
        route = "your_groups",
        startRoute = SavedGroupsScreenDestination,
        destinations = listOf(
            GroupSubjectsScreenDestination,
            OtherActivitiesScreenDestination,
            SavedGroupsScreenDestination
        )
    )

    val main = NavGraph(
        route = "main",
        startRoute = ScheduleScreenDestination,
        destinations = listOf(
            SearchScreenDestination,
            CreateActivityScreenDestination,
            ScheduleScreenDestination,
            YourGroupsScreenDestination
        ),
        nestedNavGraphs = listOf(
            yourGroups
        )
    )

    val root = NavGraph(
        route = "root",
        startRoute = MainScreenDestination,
        destinations = listOf(
            MainScreenDestination,
            PreferencesScreenDestination,
            SchedulePreviewScreenDestination
        ),
        nestedNavGraphs = listOf(
            main
        )
    )
}
