package com.github.pwoicik.uekschedule.domain.model

data class Subject(

    val groupId: Long,
    val groupName: String,
    val name: String,
    val type: String,
    val isIgnored: Boolean
)
