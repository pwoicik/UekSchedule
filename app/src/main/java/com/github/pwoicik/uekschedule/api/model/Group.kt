package com.github.pwoicik.uekschedule.api.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "plan-zajec", strict = false)
data class GroupsRoot @JvmOverloads constructor(

    @field:ElementList(name = "zasob", inline = true)
    var groups: List<Group>? = null
)

@Root(name = "zasob", strict = false)
data class Group @JvmOverloads constructor(

    @field:Attribute(name = "id")
    var id: Long = 0L,

    @field:Attribute(name = "nazwa")
    var name: String = ""
)
