package com.github.pwoicik.uekschedule.feature_schedule.presentation.screen.schedule

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry

data class ScheduleScreenState(
    val hasSavedGroups: Boolean = false,
    val entries: List<ScheduleEntry> = emptyList(),
    val isUpdating: Boolean = false
)
