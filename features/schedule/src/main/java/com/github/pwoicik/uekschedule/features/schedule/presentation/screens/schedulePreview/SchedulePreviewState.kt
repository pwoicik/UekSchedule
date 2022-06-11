package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview

import androidx.compose.ui.text.input.TextFieldValue
import com.github.pwoicik.uekschedule.domain.model.ScheduleEntry
import java.time.LocalDate

internal data class SchedulePreviewState(

    val groupName: String,
    val didTry: Boolean = false,
    val entries: List<ScheduleEntry>? = null,
    val filteredEntries: Map<LocalDate, List<ScheduleEntry>> = emptyMap(),
    val searchValue: TextFieldValue = TextFieldValue(),
    val isRefreshing: Boolean = false
)
