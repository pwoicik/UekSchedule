package com.github.pwoicik.uekschedule.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithClassesEntity(

    @Embedded
    val group: GroupEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "group_id"
    )
    val classes: List<ClassEntity>
)
