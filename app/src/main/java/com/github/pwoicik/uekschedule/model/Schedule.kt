package com.github.pwoicik.uekschedule.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "plan-zajec", strict = false)
data class Schedule @JvmOverloads constructor(

    @field:Attribute(name = "id")
    var groupId: String = "",

    @field:Attribute(name = "nazwa")
    var groupName: String = "",

    @field:ElementList(name = "zajecia", inline = true)
    var classes: List<Class>? = null,
)
