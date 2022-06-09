package com.github.pwoicik.uekschedule.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.ZonedDateTime

@Entity(
    tableName = "classes",
    primaryKeys = ["schedulable_id", "start_datetime"],
    foreignKeys = [
        ForeignKey(
            entity = SchedulableEntity::class,
            parentColumns = ["id"],
            childColumns = ["schedulable_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClassEntity(

    @ColumnInfo(name = "schedulable_id")
    val schedulableId: Long = 0L,

    val subject: String,

    @ColumnInfo(name = "start_datetime")
    val startDateTime: ZonedDateTime,

    @ColumnInfo(name = "end_datetime")
    val endDateTime: ZonedDateTime,

    val type: String,

    val details: String? = null,

    val teachers: List<String>? = null,

    @ColumnInfo(defaultValue = "null")
    val groups: List<String>? = null,

    val location: String? = null
)
