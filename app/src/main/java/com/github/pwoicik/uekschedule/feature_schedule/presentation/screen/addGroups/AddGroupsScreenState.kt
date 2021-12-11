package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addGroups

import android.util.Log
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

data class AddGroupsScreenState(
    val availableGroupsState: AvailableGroupsState = AvailableGroupsState(isLoading = true),

    val selectedGroups: List<Group> = emptyList(),
    val searchText: String = "",
    val isSaving: Boolean = false,
    val isSavingSuccess: Boolean = false,
    val isSavingError: Boolean = false
) {

    val filteredGroups: List<Group>
        get() {
            Log.d("oh no", "very long calculation ${count++}")
            return availableGroupsState.groups.filter { group ->
                searchText.lowercase() in group.name.lowercase() &&
                        group !in selectedGroups
            }
        }
}

var count = 1
