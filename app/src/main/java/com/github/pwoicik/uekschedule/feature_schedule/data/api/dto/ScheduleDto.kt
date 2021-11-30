package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "plan-zajec", strict = false)
data class ScheduleDto @JvmOverloads constructor(

    @field:Attribute(name = "id")
    var groupId: String = "",

    @field:Attribute(name = "nazwa")
    var groupName: String = "",

    @field:ElementList(name = "zajecia", inline = true, required = false)
    var classes: List<ClassDto>? = null,
)
