package com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.api.dto.ClassDto
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import com.github.pwoicik.uekschedule.feature_schedule.data.util.convertDateTime

fun ClassDto.toClass(groupId: Long): Class {
    val endTime = endTime.dropLast(6)

    return Class(
        groupId = groupId,
        subject = subject,
        startDateTime = convertDateTime(date, startTime),
        endDateTime = convertDateTime(date, endTime),
        type = type,
        details = details,
        teachers = teachers,
        location = location
    )
}
