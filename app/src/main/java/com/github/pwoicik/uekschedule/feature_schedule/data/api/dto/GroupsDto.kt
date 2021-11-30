package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "plan-zajec", strict = false)
data class GroupsDto @JvmOverloads constructor(

    @field:ElementList(name = "zasob", inline = true)
    var groups: List<GroupDto>? = null
)
