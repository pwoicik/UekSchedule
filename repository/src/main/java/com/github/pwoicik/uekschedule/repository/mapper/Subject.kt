package com.github.pwoicik.uekschedule.repository.mapper

import com.github.pwoicik.uekschedule.data.db.entity.SubjectEntity
import com.github.pwoicik.uekschedule.domain.model.Subject

internal fun SubjectEntity.toSubject(isIgnored: Boolean) = Subject(
    schedulableId, schedulableName, name, type, isIgnored
)

internal fun Subject.toSubjectEntity() = SubjectEntity(groupId, groupName, name, type)
