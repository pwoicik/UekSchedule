package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule

sealed class ScheduleDestinationEvent {

    data class SearchTextChanged(val newText: String) : ScheduleDestinationEvent()
    object FabClicked : ScheduleDestinationEvent()
    object RefreshButtonClicked : ScheduleDestinationEvent()
}
