package com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList

import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDate

fun List<ScheduleEntry>.filterEntries(filterText: String): Map<LocalDate, List<ScheduleEntry>> {
    return this.filter { entry ->
        val matchesName = entry.name.contains(filterText, ignoreCase = true)
        val matchesTeacher = entry.teachers.any { teacher ->
            teacher.contains(filterText, ignoreCase = true)
        }
        matchesName || matchesTeacher
    }.groupBy(ScheduleEntry::startDate)
}

fun Map<LocalDate, List<ScheduleEntry>>.firstVisibleItemIndex(date: LocalDate): Int {
    var idx = 0
    for ((day, entries) in this) {
        if (day >= date) break
        idx += 1 + entries.size
    }
    return idx
}
