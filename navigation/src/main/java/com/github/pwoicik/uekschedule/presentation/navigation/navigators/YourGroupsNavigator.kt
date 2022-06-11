package com.github.pwoicik.uekschedule.presentation.navigation.navigators

import com.github.pwoicik.uekschedule.domain.model.SchedulableType
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.destinations.CreateActivityScreenDestination
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.otherActivities.OtherActivitiesNavigator
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects.destinations.GroupSubjectsScreenDestination
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups.SavedGroupsNavigator
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.SchedulePreviewScreenDestination
import com.github.pwoicik.uekschedule.features.search.presentation.screens.search.destinations.SearchScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class YourGroupsNavigator(
    private val yourGroupsNavController: DestinationsNavigator,
    private val mainNavController: DestinationsNavigator,
    private val rootNavController: DestinationsNavigator
) : OtherActivitiesNavigator, SavedGroupsNavigator {
    override fun openCreateActivity(activityId: Long) {
        mainNavController.navigate(CreateActivityScreenDestination(activityId))
    }

    override fun openSearch() {
        mainNavController.navigate(SearchScreenDestination)
    }

    override fun openGroupSubjects(groupId: Long, groupName: String) {
        yourGroupsNavController.navigate(GroupSubjectsScreenDestination(groupId, groupName))
    }

    override fun openSchedulePreview(
        schedulableId: Long,
        schedulableName: String,
        schedulableType: SchedulableType
    ) {
        rootNavController.navigate(
            SchedulePreviewScreenDestination(
                schedulableId,
                schedulableName,
                schedulableType
            )
        )
    }
}
