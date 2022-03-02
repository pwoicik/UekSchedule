package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import java.time.LocalDate

data class ScheduleScreenState(
    val hasSavedGroups: Boolean = false,
    val entries: List<ScheduleEntry> = emptyList(),
    val isRefreshing: Boolean = false,
    val searchText: String = ""
)
