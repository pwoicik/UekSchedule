package com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.api.dto.ClassDto
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import com.github.pwoicik.uekschedule.feature_schedule.data.util.convertDateTime

fun List<ClassDto>.toClasses(groupId: Long): List<Class> = this
    .filter { dto ->
        val isInvalid = dto.date == null || dto.startTime == null || dto.endTime == null
        !isInvalid
    }
    .map { dto ->
        val endTime = dto.endTime!!.replace(""" \(.+\)""".toRegex(), "")
        Class(
            groupId = groupId,
            subject = dto.subject ?: dto.type!!.replaceFirstChar { it.uppercaseChar() },
            startDateTime = convertDateTime(dto.date!!, dto.startTime!!),
            endDateTime = convertDateTime(dto.date!!, endTime),
            type = dto.type!!,
            details = dto.details,
            teachers = if (dto.teachers?.isEmpty() == true) null else dto.teachers,
            location = dto.location
        )
    }
