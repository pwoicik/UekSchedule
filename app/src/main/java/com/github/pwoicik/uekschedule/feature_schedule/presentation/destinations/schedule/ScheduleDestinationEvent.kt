package com.github.pwoicik.uekschedule.feature_schedule.presentation.destinations.schedule

import androidx.compose.ui.text.input.TextFieldValue

sealed class ScheduleDestinationEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : ScheduleDestinationEvent()
    object FabClicked : ScheduleDestinationEvent()
    object RefreshButtonClicked : ScheduleDestinationEvent()
}
