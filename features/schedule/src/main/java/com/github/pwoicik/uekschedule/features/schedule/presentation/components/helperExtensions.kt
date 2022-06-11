package com.github.pwoicik.uekschedule.features.schedule.presentation.components

import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDate

internal fun List<ScheduleEntry>.filterEntries(filterText: String): Map<LocalDate, List<ScheduleEntry>> {
    return this.filter { entry ->
        val matchesName = entry.name.contains(filterText, ignoreCase = true)
        val matchesTeacher by lazy {
            entry.teachers.any { teacher ->
                teacher.contains(filterText, ignoreCase = true)
            }
        }
        val matchesGroup by lazy {
            entry.groups.any { group ->
                group.contains(filterText, ignoreCase = true)
            }
        }
        matchesName || matchesTeacher || matchesGroup
    }.groupBy(ScheduleEntry::startDate)
}

internal fun Map<LocalDate, List<ScheduleEntry>>.firstVisibleItemIndex(date: LocalDate): Int {
    var idx = 0
    for ((day, entries) in this) {
        if (day >= date) break
        idx += 1 + entries.size
    }
    return idx
}
