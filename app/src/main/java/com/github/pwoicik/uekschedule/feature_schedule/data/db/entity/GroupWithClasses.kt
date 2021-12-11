package com.github.pwoicik.uekschedule.feature_schedule.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithClasses(

    @Embedded
    val group: Group,

    @Relation(
        parentColumn = "schedules",
        entityColumn = "classes"
    )
    val classes: List<Class>
)
