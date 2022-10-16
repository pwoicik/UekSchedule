package com.github.pwoicik.uekschedule.data.api.dto

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "plan-zajec")
data class SchedulablesDto(

    @Element
    val schedulables: List<SchedulableDto>? = null
)
