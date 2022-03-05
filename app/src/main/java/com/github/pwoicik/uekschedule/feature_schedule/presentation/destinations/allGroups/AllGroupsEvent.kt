package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

sealed class AllGroupsEvent {

    data class SearchTextChanged(val newText: String) : AllGroupsEvent()
    data class GroupCardClicked(val group: Group) : AllGroupsEvent()
    data class GroupSaveButtonClicked(val group: Group) : AllGroupsEvent()
    object RetryGroupsFetch : AllGroupsEvent()
}
