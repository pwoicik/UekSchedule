package com.github.pwoicik.uekschedule.feature_schedule.data.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun convertDateTime(
    date: String,
    time: String,
    fromZone: ZoneId = ZoneId.of("Europe/Warsaw"),
    toZone: ZoneId = ZoneId.systemDefault()
): ZonedDateTime {
    val ldt = LocalDateTime.parse("${date}T${time}")
    val zdt = ZonedDateTime.of(ldt, fromZone)
    return zdt.withZoneSameInstant(toZone)
}
