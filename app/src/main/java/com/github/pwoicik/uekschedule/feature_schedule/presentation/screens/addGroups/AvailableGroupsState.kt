package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addGroups

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

data class AvailableGroupsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val groups: List<Group> = emptyList()
)
