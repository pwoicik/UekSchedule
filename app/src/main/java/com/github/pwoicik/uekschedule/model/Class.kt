package com.github.pwoicik.uekschedule.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.time.ZonedDateTime

@Root(name = "zajecia", strict = false)
data class Class @JvmOverloads constructor(

    @field:Element(name = "przedmiot")
    var subject: String = "",

    @field:Element(name = "termin")
    var date: String = "",

    @field:Element(name = "od-godz")
    var startTime: String = "",

    @field:Element(name = "do-godz")
    var endTime: String = "",

    @field:Element(name = "typ")
    var type: String = "",

    @field:Element(name = "uwagi", required = false)
    var details: String? = null,

    @field:Element(name = "nauczyciel")
    var teacher: String = "",

    @field:Element(name = "sala", required = false)
    var location: String? = null,
) {
    val startDate: ZonedDateTime
        get() = ZonedDateTime.parse("${date}T${startTime}:00+01:00[Europe/Warsaw]")

    val endDate: ZonedDateTime
        get() = ZonedDateTime.parse("${date}T${endTime}:00+01:00[Europe/Warsaw]")
}
