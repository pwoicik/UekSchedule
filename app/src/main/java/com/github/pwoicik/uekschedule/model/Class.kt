package com.github.pwoicik.uekschedule.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.time.*

@Root(name = "zajecia", strict = false)
data class Class @JvmOverloads constructor(

    @field:Element(name = "przedmiot")
    var subject: String = "",

    @field:Element(name = "termin")
    var _date: String = "",

    @field:Element(name = "od-godz")
    var _startTime: String = "",

    @field:Element(name = "do-godz")
    var _endTime: String = "",

    @field:Element(name = "typ")
    var type: String = "",

    @field:Element(name = "uwagi", required = false)
    var details: String? = null,

    @field:Element(name = "nauczyciel")
    var teacher: String = "",

    @field:Element(name = "sala", required = false)
    var location: String? = null,
) {
    val startZonedDateTime: ZonedDateTime by lazy {
        convertDateTime(_date, _startTime)
    }

    val startDate: LocalDate by lazy {
        startZonedDateTime.toLocalDate()
    }

    val startTime: LocalTime by lazy {
        startZonedDateTime.toLocalTime()
    }

    val endZonedDateTime: ZonedDateTime by lazy {
        convertDateTime(_date, _endTime)
    }

    val endDate: LocalDate by lazy {
        endZonedDateTime.toLocalDate()
    }

    val endTime: LocalTime by lazy {
        endZonedDateTime.toLocalTime()
    }
}

private fun convertDateTime(
    date: String,
    time: String,
    zone: ZoneId = ZoneId.of("Europe/Warsaw"),
    toZone: ZoneId = ZoneId.systemDefault()
): ZonedDateTime {
    val ldt = LocalDateTime.parse("${date}T${time}")
    val zdt = ZonedDateTime.of(ldt, zone)
    return zdt.withZoneSameInstant(toZone)
}
