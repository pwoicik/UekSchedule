package com.github.pwoicik.uekschedule.features.schedule.presentation.components

import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

internal sealed class ScheduleEntryStatus {
    data class NotStarted(val minutesToStart: Long) : ScheduleEntryStatus()
    data class InProgress(val minutesRemaining: Long) : ScheduleEntryStatus()
    data object Ended : ScheduleEntryStatus()
}

internal fun ScheduleEntry.status(timeNow: LocalDateTime): ScheduleEntryStatus {
    return when {
        timeNow.isBefore(startDateTime) -> ScheduleEntryStatus.NotStarted(
            timeNow.until(startDateTime, ChronoUnit.MINUTES)
        )
        timeNow.isBefore(endDateTime) -> ScheduleEntryStatus.InProgress(
            timeNow.until(endDateTime, ChronoUnit.MINUTES)
        )
        else -> ScheduleEntryStatus.Ended
    }
}
