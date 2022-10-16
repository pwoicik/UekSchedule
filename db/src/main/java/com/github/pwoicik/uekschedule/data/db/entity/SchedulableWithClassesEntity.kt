package com.github.pwoicik.uekschedule.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SchedulableWithClassesEntity(

    @Embedded
    val schedulable: SchedulableEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "schedulable_id"
    )
    val classes: List<ClassEntity>
)
