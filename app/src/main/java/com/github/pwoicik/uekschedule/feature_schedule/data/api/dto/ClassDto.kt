package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "zajecia", strict = false)
data class ClassDto(

    @field:Element(name = "przedmiot", required = false)
    var subject: String? = null,

    @field:Element(name = "termin", required = false)
    var date: String? = null,

    @field:Element(name = "od-godz", required = false)
    var startTime: String? = null,

    @field:Element(name = "do-godz", required = false)
    var endTime: String? = null,

    @field:Element(name = "typ", required = false)
    var type: String? = null,

    @field:Element(name = "uwagi", required = false)
    var details: String? = null,

    @field:ElementList(entry = "nauczyciel", inline = true, required = false)
    var teachers: List<String>? = null,

    @field:Element(name = "sala", required = false)
    var location: String? = null,
)
