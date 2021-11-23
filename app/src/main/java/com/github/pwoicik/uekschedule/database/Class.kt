package com.github.pwoicik.uekschedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity(tableName = "classes", primaryKeys = ["group_id", "start_datetime"])
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

    val teacher: String,

    val location: String? = null
) {

    @delegate:Ignore
    val startDate: LocalDate by lazy {
        startDateTime.toLocalDate()
    }
}
