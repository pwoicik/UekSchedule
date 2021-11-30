package com.github.pwoicik.uekschedule.feature_schedule.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(

    @PrimaryKey
    val id: Long,

    val name: String
)
