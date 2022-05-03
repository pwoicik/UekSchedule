package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.db.entity.ClassEntity
import com.github.pwoicik.uekschedule.data.db.entity.GroupWithClassesEntity
import com.github.pwoicik.uekschedule.domain.model.GroupWithClasses

internal fun GroupWithClassesEntity.toGroupWithClasses() = GroupWithClasses(
    group.toGroup(), classes.map(ClassEntity::toClass)
)
