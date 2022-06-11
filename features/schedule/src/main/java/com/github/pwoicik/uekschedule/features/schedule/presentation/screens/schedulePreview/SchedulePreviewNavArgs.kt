package com.github.pwoicik.uekschedule.features.schedule.presentation.screens.schedulePreview

import com.github.pwoicik.uekschedule.domain.model.SchedulableType

data class SchedulePreviewNavArgs(
    val schedulableId: Long,
    val schedulableName: String,
    val schedulableType: SchedulableType
)
