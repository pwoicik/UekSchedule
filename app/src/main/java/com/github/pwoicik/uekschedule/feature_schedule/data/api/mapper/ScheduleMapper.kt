package com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper

import com.github.pwoicik.uekschedule.feature_schedule.data.api.dto.ScheduleDto
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses

fun ScheduleDto.toGroupWithClasses(): GroupWithClasses {
    val groupId = groupId.toLong()

    val schedule = Group(
        groupId,
        groupName
    )

    val classes = classes?.toClasses(groupId) ?: emptyList()

    return GroupWithClasses(schedule, classes)
}
