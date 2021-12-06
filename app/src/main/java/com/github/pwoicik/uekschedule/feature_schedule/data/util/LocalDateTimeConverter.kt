package com.github.pwoicik.uekschedule.feature_schedule.data.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object LocalDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(timestamp: Long): ZonedDateTime {
        val instant = Instant.ofEpochSecond(timestamp)
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    @TypeConverter
    @JvmStatic
    fun zonedDateTimeToTimestamp(dateTime: ZonedDateTime): Long = dateTime.toEpochSecond()
}
