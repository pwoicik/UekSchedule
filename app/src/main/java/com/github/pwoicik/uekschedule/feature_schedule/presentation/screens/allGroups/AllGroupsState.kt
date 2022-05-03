package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.allGroups

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.domain.model.Group

data class AllGroupsState(

    val isLoading: Boolean = false,
    val didTry: Boolean = false,
    val isSaving: Boolean = false,
    val groups: List<Group>? = null,
    val filteredGroups: List<Group> = emptyList(),
    val searchValue: TextFieldValue = TextFieldValue()
)
