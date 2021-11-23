package com.github.pwoicik.uekschedule.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(

    @PrimaryKey
    val id: Long,

    val name: String
)
