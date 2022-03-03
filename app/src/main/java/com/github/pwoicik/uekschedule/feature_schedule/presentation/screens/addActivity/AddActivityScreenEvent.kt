package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.addActivity

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed class AddActivityScreenEvent {

    data class NameChanged(val value: String) : AddActivityScreenEvent()
    data class LocationChanged(val value: String) : AddActivityScreenEvent()
    data class TypeChanged(val value: String) : AddActivityScreenEvent()
    data class TeacherChanged(val value: String) : AddActivityScreenEvent()
    data class StartTimeChanged(val value: LocalTime) : AddActivityScreenEvent()
    data class DurationMinutesChanged(val value: String) : AddActivityScreenEvent()
    data class StartDateChanged(val value: LocalDate) : AddActivityScreenEvent()
    object RepeatActivityChanged : AddActivityScreenEvent()
    data class AddDayOfWeekToRepeat(val value: DayOfWeek) : AddActivityScreenEvent()
    data class RemoveDayOfWeekToRepeat(val value: DayOfWeek) : AddActivityScreenEvent()
    data class RepeatOnDaysOfWeekChanged(val value: Set<DayOfWeek>) : AddActivityScreenEvent()

    object SaveActivity : AddActivityScreenEvent()
}
