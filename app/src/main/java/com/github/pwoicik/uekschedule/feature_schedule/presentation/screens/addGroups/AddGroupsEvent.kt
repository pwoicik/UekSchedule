package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

sealed class AddGroupsEvent {
    data class SelectGroup(val group: Group) : AddGroupsEvent()
    data class UnselectGroup(val group: Group) : AddGroupsEvent()
    data class SearchTextChange(val text: String) : AddGroupsEvent()
    object ClearSelectedGroups : AddGroupsEvent()
    object SaveSelectedGroups : AddGroupsEvent()
    object RetryGetAvailableGroups : AddGroupsEvent()
    object RetrySaveSelectedGroups : AddGroupsEvent()
}
