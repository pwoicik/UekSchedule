package com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.api.dto.GroupDto
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group

fun GroupDto.toGroup(): Group {
    return Group(id, name)
}
