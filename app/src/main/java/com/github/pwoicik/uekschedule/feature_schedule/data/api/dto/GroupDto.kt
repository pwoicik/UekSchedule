package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "zasob")
data class GroupDto(

    @Attribute(name = "id")
    val id: Long,

    @Attribute(name = "nazwa")
    val name: String
)
