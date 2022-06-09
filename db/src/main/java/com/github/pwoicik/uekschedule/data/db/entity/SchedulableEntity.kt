package com.github.pwoicik.uekschedule.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.pwoicik.uekschedule.domain.model.SchedulableType

@Entity(tableName = "schedulables")
data class SchedulableEntity(

    @PrimaryKey
    val id: Long,

    val name: String,

    @ColumnInfo(defaultValue = "0", index = true)
    val type: SchedulableType,

    @ColumnInfo(name = "is_favorite", defaultValue = "1")
    val isFavorite: Boolean = true
)
