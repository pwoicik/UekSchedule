package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry

data class ScheduleDestinationState(

    val hasSavedGroups: Boolean? = null,
    val entries: List<ScheduleEntry>? = null,
    val isRefreshing: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue()
)
