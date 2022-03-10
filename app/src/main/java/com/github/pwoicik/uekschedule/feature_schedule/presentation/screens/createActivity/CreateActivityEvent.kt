package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.createActivity

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed class CreateActivityEvent {

    data class NameChanged(val value: String) : CreateActivityEvent()
    data class LocationChanged(val value: String) : CreateActivityEvent()
    data class TypeChanged(val value: String) : CreateActivityEvent()
    data class TeacherChanged(val value: String) : CreateActivityEvent()
    data class StartTimeChanged(val value: LocalTime) : CreateActivityEvent()
    data class DurationMinutesChanged(val value: String) : CreateActivityEvent()
    data class StartDateChanged(val value: LocalDate) : CreateActivityEvent()
    object RepeatActivityChanged : CreateActivityEvent()
    data class AddDayOfWeekToRepeat(val value: DayOfWeek) : CreateActivityEvent()
    data class RemoveDayOfWeekToRepeat(val value: DayOfWeek) : CreateActivityEvent()
    data class RepeatOnDaysOfWeekChanged(val value: Set<DayOfWeek>) : CreateActivityEvent()

    object SaveActivity : CreateActivityEvent()
}
