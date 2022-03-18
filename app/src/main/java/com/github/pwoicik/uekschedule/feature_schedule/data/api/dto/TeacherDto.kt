package com.github.pwoicik.uekschedule.feature_schedule.data.api.dto

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "nauczyciel")
data class TeacherDto(

    @TextContent
    val teacher: String
)
