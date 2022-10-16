package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.db.entity.ClassEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableWithClassesEntity
import com.github.pwoicik.uekschedule.domain.model.SchedulableWithClasses

internal fun SchedulableWithClassesEntity.toSchedulableWithClasses() = SchedulableWithClasses(
    schedulable.toSchedulable(), classes.map(ClassEntity::toClass)
)
