package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule

import androidx.compose.ui.text.input.TextFieldValue

sealed class ScheduleEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : ScheduleEvent()
    object FabClicked : ScheduleEvent()
    object RefreshButtonClicked : ScheduleEvent()
}
