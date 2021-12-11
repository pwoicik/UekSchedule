package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen

sealed class Screen(val route: String) {
    object ScheduleScreen : Screen("schedule_screen")
    object ManageGroupsScreen : Screen("manage_groups_screen")
    object AddGroupsScreen : Screen("add_groups_screen")
    object ManageActivitiesScreen : Screen("manage_activities_screen")
    object AddActivityScreen : Screen("add_activity_screen")
}
