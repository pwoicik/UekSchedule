package com.github.pwoicik.uekschedule.feature_schedule.data.util

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ZonedDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toZonedDateTime(value: String): ZonedDateTime = ZonedDateTime.parse(value)

    @TypeConverter
    @JvmStatic
    fun fromZonedDateTime(dateTime: ZonedDateTime): String = dateTime.format(formatter)
}
