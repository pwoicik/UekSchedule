package com.github.pwoicik.uekschedule.feature_schedule.data.db.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun Activity.toScheduleEntries(): List<ScheduleEntry> {
    val dateNow = LocalDate.now()
    val maxDateTime = LocalDateTime.of(dateNow, LocalTime.MIDNIGHT).plusMonths(6)

    if (repeatOnDaysOfWeek == null) {
        val startDateTime = startDateTime.toLocalDateTime()
        return if (startDateTime.isBefore(maxDateTime)) {
            listOf(
                ScheduleEntry(
                    name = name,
                    startDateTime = startDateTime,
                    endDateTime = startDateTime.plusMinutes(durationMinutes),
                    type = type,
                    details = null,
                    teachers = teacher?.let { listOf(teacher) },
                    location = location
                )
            )
        } else {
            emptyList()
        }
    } else {
        val entries = mutableListOf<ScheduleEntry>()
        repeatOnDaysOfWeek
            // first specified dayOfWeek from current date
            .map { dayOfWeek ->
                val dayDifference = dayOfWeek.value - dateNow.dayOfWeek.value
                if (dayDifference < 0) {
                    dateNow.plusDays(7L + dayDifference)
                } else {
                    dateNow.plusDays(dayDifference.toLong())
                }
            }
            .forEach { date ->
                var newDate = LocalDateTime.of(date, startDateTime.toLocalTime())
                while (newDate.isBefore(maxDateTime)) {
                    entries.add(
                        ScheduleEntry(
                            name = name,
                            startDateTime = newDate,
                            endDateTime = newDate.plusMinutes(durationMinutes),
                            type = type,
                            details = null,
                            teachers = teacher?.let { listOf(teacher) },
                            location = location
                        )
                    )

                    newDate = newDate.plusWeeks(1)
                }
            }

        return entries
    }
}
