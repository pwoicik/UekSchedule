package com.github.pwoicik.uekschedule.feature_schedule.presentation.components.scheduleEntriesList

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import java.time.LocalDate

fun Map<LocalDate, List<ScheduleEntry>>.firstVisibleItemIndex(date: LocalDate): Int {
    var idx = 0
    for ((day, entries) in this) {
        if (day >= date) break
        idx += 1 + entries.size
    }
    return idx
}
