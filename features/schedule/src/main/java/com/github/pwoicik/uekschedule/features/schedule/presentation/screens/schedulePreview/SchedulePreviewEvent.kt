package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview

import androidx.compose.ui.text.input.TextFieldValue

internal sealed class SchedulePreviewEvent {

    data class SearchTextChanged(val newValue: TextFieldValue) : SchedulePreviewEvent()
    object FabClicked : SchedulePreviewEvent()
    object RefreshButtonClicked : SchedulePreviewEvent()
}
