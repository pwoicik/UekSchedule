package com.github.pwoicik.uekschedule.feature_schedule.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(

    @PrimaryKey
    val id: Long,

    val name: String,

    @ColumnInfo(name = "is_favorite", defaultValue = "1")
    val isFavorite: Boolean = true
)
