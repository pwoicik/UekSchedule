package com.github.pwoicik.uekschedule.data.api.dto

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "plan-zajec")
data class ScheduleDto(

    @Attribute(name = "id")
    val schedulableId: String,

    @Attribute(name = "nazwa")
    val schedulableName: String,

    @Element
    val classes: List<ClassDto>? = null,
)
