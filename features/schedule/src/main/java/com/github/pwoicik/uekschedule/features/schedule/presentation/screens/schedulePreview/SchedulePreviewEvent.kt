package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview

import androidx.compose.ui.text.input.TextFieldValue

internal sealed class SchedulePreviewEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : SchedulePreviewEvent()
    data object FabClicked : SchedulePreviewEvent()
    data object RefreshButtonClicked : SchedulePreviewEvent()
}
