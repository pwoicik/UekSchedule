package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.db.entity.ActivityEntity
import com.github.pwoicik.uekschedule.domain.model.Activity
import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal fun ActivityEntity.toActivity() = Activity(
    id, name, location, type, teacher, startDateTime, durationMinutes, repeatOnDaysOfWeek
)

internal fun Activity.toActivityEntity() = ActivityEntity(
    id, name, location, type, teacher, startDateTime, durationMinutes, repeatOnDaysOfWeek
)

internal fun ActivityEntity.toScheduleEntries(): List<ScheduleEntry> {
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
                    teachers = listOfNotNull(teacher),
                    groups = emptyList(),
                    location = location
                )
            )
        } else {
            emptyList()
        }
    } else {
        val entries = mutableListOf<ScheduleEntry>()
        repeatOnDaysOfWeek!!
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
                            teachers = listOfNotNull(teacher),
                            groups = emptyList(),
                            location = location
                        )
                    )

                    newDate = newDate.plusWeeks(1)
                }
            }

        return entries
    }
}
