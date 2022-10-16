package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDate

@Immutable
internal data class ScheduleState(

    val hasSavedGroups: Boolean? = null,
    val entries: List<ScheduleEntry>? = null,
    val filteredEntries: Map<LocalDate, List<ScheduleEntry>> = emptyMap(),
    val isRefreshing: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue()
) {
    val dataState = when (hasSavedGroups) {
        true -> {
            when {
                entries == null -> DataState.NO_CONNECTION
                entries.isEmpty() -> DataState.NO_CLASSES
                filteredEntries.isEmpty() && searchValue.text.isNotBlank() -> DataState.NO_RESULTS
                else -> DataState.SUCCESS
            }
        }

        false -> DataState.NO_GROUPS
        null -> DataState.LOADING
    }
}

internal enum class DataState {
    NO_CONNECTION,
    LOADING,
    NO_GROUPS,
    NO_CLASSES,
    NO_RESULTS,
    SUCCESS
}
