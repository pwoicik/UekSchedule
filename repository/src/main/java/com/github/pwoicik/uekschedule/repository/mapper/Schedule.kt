package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.api.dto.ScheduleDto
import com.github.pwoicik.uekschedule.data.db.entity.GroupEntity
import com.github.pwoicik.uekschedule.data.db.entity.GroupWithClassesEntity

internal fun ScheduleDto.toGroupWithClasses(): GroupWithClassesEntity {
    val groupId = groupId.toLong()

    val schedule = GroupEntity(
        groupId,
        groupName
    )

    val classes = classes?.toClassEntities(groupId) ?: emptyList()

    return GroupWithClassesEntity(schedule, classes)
}
