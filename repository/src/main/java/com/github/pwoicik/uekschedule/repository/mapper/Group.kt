package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.api.dto.GroupDto
import com.github.pwoicik.uekschedule.data.db.entity.GroupEntity
import com.github.pwoicik.uekschedule.domain.model.Group

internal fun GroupDto.toGroup() = Group(id, name, isFavorite = false)

internal fun GroupEntity.toGroup() = Group(id, name, isFavorite)

internal fun Group.toGroupEntity() = GroupEntity(id, name, isFavorite)
