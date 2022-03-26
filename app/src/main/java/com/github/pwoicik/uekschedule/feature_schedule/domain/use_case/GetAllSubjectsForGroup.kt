package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Subject
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetAllSubjectsForGroup(
    private val repository: ScheduleRepository
) {

    operator fun invoke(groupId: Long): Flow<List<Subject>> {
        return repository.getAllSubjectsForGroup(groupId)
    }
}
