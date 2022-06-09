package com.github.pwoicik.uekschedule.domain.model

enum class SchedulableType {
    Group,
    Teacher
}

data class Schedulable(

    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val type: SchedulableType
)
