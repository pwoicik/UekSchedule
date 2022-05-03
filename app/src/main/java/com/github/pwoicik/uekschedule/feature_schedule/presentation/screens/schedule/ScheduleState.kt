package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedule

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDate

data class ScheduleState(

    val hasSavedGroups: Boolean? = null,
    val entries: List<ScheduleEntry>? = null,
    val filteredEntries: Map<LocalDate, List<ScheduleEntry>> = emptyMap(),
    val isRefreshing: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue()
)
