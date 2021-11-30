package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetSavedGroupsFlow(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<List<Group>> {
        return repository.getSavedGroupsFlow()
    }
}
