package com.github.pwoicik.uekschedule.domain.model

data class SchedulableWithClasses(

    val group: Schedulable,
    val classes: List<Class>
)
