package com.github.pwoicik.uekschedule.data.api.dto

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

@Xml(name = "zajecia")
data class ClassDto(

    @PropertyElement(name = "przedmiot")
    val subject: String? = null,

    @PropertyElement(name = "termin")
    val date: String,

    @PropertyElement(name = "od-godz")
    val startTime: String,

    @PropertyElement(name = "do-godz")
    val endTime: String,

    @PropertyElement(name = "typ")
    val type: String,

    @PropertyElement(name = "uwagi")
    val details: String? = null,

    @Element
    val teachers: List<TeacherDto>? = null,

    @PropertyElement(name = "grupa")
    val groups: String? = null,

    @PropertyElement(name = "sala", converter = HtmlEscapeStringConverter::class)
    val location: String,
)
