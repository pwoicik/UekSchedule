package com.github.pwoicik.uekschedule.feature_schedule.data.db.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry

fun Class.toScheduleEntry(): ScheduleEntry {
    return ScheduleEntry(
        name = subject,
        startDateTime = startDateTime.toLocalDateTime(),
        endDateTime = endDateTime.toLocalDateTime(),
        type = type,
        details = details,
        teachers = teachers,
        location = location
    )
}
