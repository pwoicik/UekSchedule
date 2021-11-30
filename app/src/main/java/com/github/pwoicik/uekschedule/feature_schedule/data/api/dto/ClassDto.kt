package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "zajecia", strict = false)
data class ClassDto(

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

    @field:ElementList(entry = "nauczyciel", inline = true)
    var teachers: List<String>? = null,

    @field:Element(name = "sala", required = false)
    var location: String? = null,
)
