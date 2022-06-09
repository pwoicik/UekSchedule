package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.api.dto.ScheduleDto
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableWithClassesEntity
import com.github.pwoicik.uekschedule.domain.model.SchedulableType

internal fun ScheduleDto.toGroupWithClasses(): SchedulableWithClassesEntity {
    val groupId = groupId.toLong()

    val schedule = SchedulableEntity(
        groupId,
        groupName,
        type = SchedulableType.Group
    )

    val classes = classes?.toClassEntities(groupId) ?: emptyList()

    return SchedulableWithClassesEntity(schedule, classes)
}
