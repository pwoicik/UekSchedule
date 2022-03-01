package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetSavedGroupsCount(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<Int> {
        return repository.getSavedGroupsCount()
    }
}
