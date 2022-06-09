package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedule

import androidx.compose.ui.text.input.TextFieldValue

internal sealed class ScheduleEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : ScheduleEvent()
    object FabClicked : ScheduleEvent()
    object RefreshButtonClicked : ScheduleEvent()
}
