package com.github.pwoicik.uekschedule.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.ZonedDateTime

@Entity(tableName = "activities")
data class ActivityEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,

    val location: String?,

    val type: String?,

    val teacher: String?,

    @ColumnInfo(name = "start_datetime")
    val startDateTime: ZonedDateTime,

    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Long,

    @ColumnInfo(name = "repeat_on_days_of_week")
    val repeatOnDaysOfWeek: List<DayOfWeek>?
)
