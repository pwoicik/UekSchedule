package com.github.pwoicik.uekschedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class Schedule(

    @PrimaryKey
    @ColumnInfo(name = "group_id")
    val groupID: Long,

    @ColumnInfo(name = "group_name")
    val groupName: String
)
