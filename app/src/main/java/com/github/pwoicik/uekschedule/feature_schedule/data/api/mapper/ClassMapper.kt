package com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.api.dto.ClassDto
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import com.github.pwoicik.uekschedule.feature_schedule.data.util.convertDateTime

fun List<ClassDto>.toClasses(groupId: Long): List<Class> = this
    .filter { dto ->
        val isInvalid = dto.date.isEmpty() || dto.startTime.isEmpty() || dto.endTime.isEmpty()
        !isInvalid
    }
    .map { dto ->
        val endTime = dto.endTime.replace(""" \(.+\)""".toRegex(), "")
        val teachers = dto.teachers
            .filter { it.teacher.isNotEmpty() }
            .map { it.teacher }
            .ifEmpty { null }

        Class(
            groupId = groupId,
            subject = dto.subject ?: dto.type.replaceFirstChar(Char::uppercase),
            startDateTime = convertDateTime(dto.date, dto.startTime),
            endDateTime = convertDateTime(dto.date, endTime),
            type = dto.type,
            details = dto.details,
            teachers = teachers,
            location = dto.location.ifEmpty { null }
        )
    }
