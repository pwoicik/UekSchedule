package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedulePreview

import androidx.compose.ui.text.input.TextFieldValue

sealed class SchedulePreviewEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : SchedulePreviewEvent()
    object FabClicked : SchedulePreviewEvent()
    object RefreshButtonClicked : SchedulePreviewEvent()
}
