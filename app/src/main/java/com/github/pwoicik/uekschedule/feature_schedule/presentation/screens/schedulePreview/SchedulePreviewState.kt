package com.github.pwoicik.uekschedule.feature_schedule.presentation.screens.schedulePreview

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry

data class SchedulePreviewState(

    val groupName: String,
    val entries: List<ScheduleEntry>? = null,
    val isRefreshing: Boolean = false
)
