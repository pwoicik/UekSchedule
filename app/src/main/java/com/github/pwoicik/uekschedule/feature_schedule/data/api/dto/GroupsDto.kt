package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "plan-zajec")
data class GroupsDto(

    @Element
    val groups: List<GroupDto>? = null
)
