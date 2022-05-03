package com.github.pwoicik.uekschedule.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class ScheduleEntry(

    val name: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val type: String?,
    val details: String?,
    val teachers: List<String>,
    val location: String?,
) {

    val startDate: LocalDate by lazy {
        startDateTime.toLocalDate()
    }
}
