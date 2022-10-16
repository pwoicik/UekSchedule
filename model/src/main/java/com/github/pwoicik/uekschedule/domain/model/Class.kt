package com.github.pwoicik.uekschedule.domain.model

import java.time.ZonedDateTime

data class Class(

    val schedulableId: Long = 0L,
    val subject: String,
    val startDateTime: ZonedDateTime,
    val endDateTime: ZonedDateTime,
    val type: String,
    val details: String? = null,
    val teachers: List<String>? = null,
    val groups: List<String>? = null,
    val location: String? = null
)
