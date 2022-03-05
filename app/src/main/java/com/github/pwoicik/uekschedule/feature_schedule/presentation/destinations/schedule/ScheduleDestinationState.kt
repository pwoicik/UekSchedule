package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import java.time.LocalDate

data class ScheduleDestinationState(

    val hasSavedGroups: Boolean = true,
    val entries: List<ScheduleEntry>? = null,
    val isRefreshing: Boolean = false,
    val searchText: String = ""
) {

    val filteredEntries: Map<LocalDate, List<ScheduleEntry>>?
        get() = entries?.filter { entry ->
            val matchesName = entry.name.contains(searchText, ignoreCase = true)
            val matchesTeacher = entry.teachers?.any { teacher ->
                teacher.contains(searchText, ignoreCase = true)
            } ?: false
            matchesName || matchesTeacher
        }?.groupBy(ScheduleEntry::startDate)
}
