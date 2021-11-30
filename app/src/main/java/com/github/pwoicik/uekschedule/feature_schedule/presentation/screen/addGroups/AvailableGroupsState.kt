package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group

data class AvailableGroupsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val groups: List<Group> = emptyList()
)
