package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.api.dto.ScheduleDto
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableWithClassesEntity
import com.github.pwoicik.uekschedule.domain.model.SchedulableType

internal fun ScheduleDto.toSchedulableWithClasses(
    type: SchedulableType
): SchedulableWithClassesEntity {
    val groupId = schedulableId.toLong()

    val schedule = SchedulableEntity(
        groupId,
        schedulableName,
        type
    )

    val classes = classes?.toClassEntities(groupId) ?: emptyList()

    return SchedulableWithClassesEntity(schedule, classes)
}
