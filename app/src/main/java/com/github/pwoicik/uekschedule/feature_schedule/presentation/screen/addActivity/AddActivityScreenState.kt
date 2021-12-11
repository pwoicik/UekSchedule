package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.addActivity

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class AddActivityScreenState(

    val name: String = "",
    val location: String = "",
    val type: String = "",
    val teacher: String = "",
    val startTime: LocalTime? = null,
    val durationMinutes: String = "90",
    val startDate: LocalDate? = null,
    val repeatActivity: Boolean = false,
    val repeatOnDaysOfWeek: Set<DayOfWeek> = emptySet()
)
