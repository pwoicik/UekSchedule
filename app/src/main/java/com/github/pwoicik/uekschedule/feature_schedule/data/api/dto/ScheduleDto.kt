package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "plan-zajec")
data class ScheduleDto(

    @Attribute(name = "id")
    val groupId: String,

    @Attribute(name = "nazwa")
    val groupName: String,

    @Element
    val classes: List<ClassDto>? = null,
)
