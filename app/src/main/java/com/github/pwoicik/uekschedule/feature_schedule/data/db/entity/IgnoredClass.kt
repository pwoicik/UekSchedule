package com.github.pwoicik.uekschedule.feature_schedule.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "ignored_classes",
    primaryKeys = [
        "group_id",
        "subject",
        "type"
    ]
)
data class IgnoredClass(

    @ColumnInfo(name = "group_id")
    val groupId: Long,

    @ColumnInfo(name = "group_name")
    val groupName: String,

    val subject: String,

    val type: String
)
