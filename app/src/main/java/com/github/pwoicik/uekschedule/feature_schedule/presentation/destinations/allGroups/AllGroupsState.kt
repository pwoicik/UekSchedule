package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

data class AllGroupsState(
    val isLoading: Boolean = false,
    val groups: List<Group>? = null,
    val searchText: String = ""
) {

    val filteredGroups: List<Group>
        get() = groups?.filter { group ->
            group.name.contains(searchText, ignoreCase = true)
        } ?: emptyList()
}
