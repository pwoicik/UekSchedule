package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

data class AllGroupsState(
    val isLoading: Boolean = false,
    val groups: List<Group>? = null,
    val searchValue: TextFieldValue = TextFieldValue()
) {

    val filteredGroups: List<Group>
        get() = groups?.filter { group ->
            group.name.contains(searchValue.text, ignoreCase = true)
        } ?: emptyList()
}
