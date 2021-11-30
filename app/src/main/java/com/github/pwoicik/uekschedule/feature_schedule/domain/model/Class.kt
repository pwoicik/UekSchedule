package com.github.pwoicik.uekschedule.feature_schedule.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity(
    tableName = "classes",
    primaryKeys = ["group_id", "start_datetime"],
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Class(

    @ColumnInfo(name = "group_id")
    val groupId: Long = 0L,

    val subject: String,

    @ColumnInfo(name = "start_datetime")
    val startDateTime: ZonedDateTime,

    @ColumnInfo(name = "end_datetime")
    val endDateTime: ZonedDateTime,

    val type: String,

    val details: String? = null,

    val teachers: String,

    val location: String? = null
) {

    @delegate:Ignore
    val startDate: LocalDate by lazy {
        startDateTime.toLocalDate()
    }
}
