package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "zasob", strict = false)
data class GroupDto @JvmOverloads constructor(

    @field:Attribute(name = "id")
    var id: Long = 0L,

    @field:Attribute(name = "nazwa")
    var name: String = ""
)
