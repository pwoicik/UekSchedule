package com.github.pwoicik.uekschedule.feature_schedule.data.db.mapper

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Subject
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.SubjectEntity

fun SubjectEntity.toSubject(isIgnored: Boolean) = Subject(groupId, groupName, name, type, isIgnored)

fun Subject.toSubjectEntity() = SubjectEntity(groupId, groupName, name, type)
