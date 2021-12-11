package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

data class AddGroupsState(
    val selectedGroups: List<Group> = emptyList(),
    val searchText: String = "",
    val filteredGroups: List<Group> = emptyList(),
    val isSaving: Boolean = false,
    val isSavingSuccess: Boolean = false,
    val isSavingError: Boolean = false
)
