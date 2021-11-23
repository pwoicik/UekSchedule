package com.github.pwoicik.uekschedule.database

import androidx.room.Embedded
import androidx.room.Relation
import com.github.pwoicik.uekschedule.api.model.Schedule as ModelSchedule

data class GroupWithClasses(

    @Embedded
    val group: Group,

    @Relation(
        parentColumn = "schedules",
        entityColumn = "classes"
    )
    val classes: List<Class>
) {
    companion object {
        fun fromModelSchedule(s: ModelSchedule): GroupWithClasses {
            val groupId = s.groupId.toLong()

            val schedule = Group(
                groupId,
                s.groupName
            )

            val classes = s.classes?.map { c ->
                c._endTime = c._endTime.dropLast(6)

                Class(
                    groupId = groupId,
                    subject = c.subject,
                    startDateTime = c.startZonedDateTime,
                    endDateTime = c.endZonedDateTime,
                    type = c.type,
                    details = c.details,
                    teacher = c.teacher,
                    location = c.location
                )
            } ?: emptyList()

            return GroupWithClasses(schedule, classes)
        }
    }
}
