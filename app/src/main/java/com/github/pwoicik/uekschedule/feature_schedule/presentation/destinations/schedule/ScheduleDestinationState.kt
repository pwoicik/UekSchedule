package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import java.time.LocalDate

data class ScheduleDestinationState(

    val hasSavedGroups: Boolean = true,
    val entries: List<ScheduleEntry>? = null,
    val isRefreshing: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue()
) {

    val filteredEntries: Map<LocalDate, List<ScheduleEntry>>?
        get() = entries?.filter { entry ->
            val matchesName = entry.name.contains(searchValue.text, ignoreCase = true)
            val matchesTeacher = entry.teachers?.any { teacher ->
                teacher.contains(searchValue.text, ignoreCase = true)
            } ?: false
            matchesName || matchesTeacher
        }?.groupBy(ScheduleEntry::startDate)
}
