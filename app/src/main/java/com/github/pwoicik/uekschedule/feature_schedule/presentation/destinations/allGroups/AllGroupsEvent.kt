package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.allGroups

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group

sealed class AllGroupsEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : AllGroupsEvent()
    data class GroupSaveButtonClicked(val group: Group) : AllGroupsEvent()
    object RetryGroupsFetch : AllGroupsEvent()
}
