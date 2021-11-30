package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen

sealed class Screen(val route: String) {
    object ClassesScreen: Screen("classes_screen")
    object ManageGroupsScreen: Screen("manage_groups_screen")
    object AddGroupsScreen: Screen("add_groups_screen")
}
