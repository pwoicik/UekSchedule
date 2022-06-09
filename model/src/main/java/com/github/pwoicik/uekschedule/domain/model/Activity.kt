package com.github.pwoicik.uekschedule.domain.model

import java.time.DayOfWeek
import java.time.ZonedDateTime

data class Activity(

    val id: Long = 0L,
    val name: String,
    val location: String?,
    val type: String?,
    val teacher: String?,
    val startDateTime: ZonedDateTime,
    val durationMinutes: Long,
    val repeatOnDaysOfWeek: List<DayOfWeek>?
) {

    val isOneshot: Boolean
        get() = repeatOnDaysOfWeek == null
}
