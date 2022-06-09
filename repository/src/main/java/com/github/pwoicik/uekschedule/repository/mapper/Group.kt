package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.api.dto.SchedulableDto
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableEntity
import com.github.pwoicik.uekschedule.domain.model.Schedulable
import com.github.pwoicik.uekschedule.domain.model.SchedulableType

internal fun SchedulableDto.toSchedulable() = Schedulable(
    id = id,
    name = name,
    isFavorite = false,
    type = SchedulableType.Group
)

internal fun SchedulableEntity.toSchedulable() = Schedulable(
    id = id,
    name = name,
    isFavorite = isFavorite,
    type = SchedulableType.Group
)

internal fun Schedulable.toGroupEntity() = SchedulableEntity(id, name, type, isFavorite)
