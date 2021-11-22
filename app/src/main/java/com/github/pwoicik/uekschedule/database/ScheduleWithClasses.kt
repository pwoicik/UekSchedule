package com.github.pwoicik.uekschedule.database

import androidx.room.Embedded
import androidx.room.Relation
import com.github.pwoicik.uekschedule.api.model.Schedule as ModelSchedule

data class ScheduleWithClasses(

    @Embedded
    val schedule: Schedule,

    @Relation(
        parentColumn = "schedules",
        entityColumn = "classes"
    )
    val classes: List<Class>
) {
    companion object {
        fun fromModelSchedule(s: ModelSchedule): ScheduleWithClasses {
            val groupId = s.groupId.toLong()

            val schedule = Schedule(
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

            return ScheduleWithClasses(schedule, classes)
        }
    }
}
