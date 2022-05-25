package com.github.pwoicik.uekschedule.presentation.navigation.navigators

import com.github.pwoicik.uekschedule.features.activities.presentation.screens.destinations.CreateActivityScreenDestination
import com.github.pwoicik.uekschedule.features.activities.presentation.screens.otherActivities.OtherActivitiesNavigator
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.groupSubjects.destinations.GroupSubjectsScreenDestination
import com.github.pwoicik.uekschedule.features.groups.presentation.screens.savedGroups.SavedGroupsNavigator
import com.github.pwoicik.uekschedule.features.schedule.presentation.screens.destinations.SingleGroupSchedulePreviewScreenDestination
import com.github.pwoicik.uekschedule.features.search.presentation.screens.allGroups.destinations.AllGroupsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class YourGroupsNavigator(
    private val navigator: DestinationsNavigator,
    private val mainNavigator: DestinationsNavigator,
    private val rootNavigator: DestinationsNavigator
) : OtherActivitiesNavigator, SavedGroupsNavigator {
    override fun openCreateActivity(activityId: Long) {
        mainNavigator.navigate(CreateActivityScreenDestination(activityId))
    }

    override fun openAllGroups() {
        mainNavigator.navigate(AllGroupsScreenDestination)
    }

    override fun openGroupSubjects(groupId: Long, groupName: String) {
        navigator.navigate(GroupSubjectsScreenDestination(groupId, groupName))
    }

    override fun openSingleGroupSchedulePreview(groupId: Long, groupName: String) {
        rootNavigator.navigate(SingleGroupSchedulePreviewScreenDestination(groupId, groupName))
    }
}
