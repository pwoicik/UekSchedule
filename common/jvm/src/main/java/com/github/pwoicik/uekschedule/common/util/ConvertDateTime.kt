package com.github.pwoicik.uekschedule.common.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun convertDateTime(
    date: String,
    time: String,
    fromZone: ZoneId = ZoneId.of("Europe/Warsaw")
): ZonedDateTime {
    val ldt = LocalDateTime.parse("${date}T${time}")
    return ZonedDateTime.of(ldt, fromZone)
}
