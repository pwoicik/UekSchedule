package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetSavedGroups(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<List<Group>> {
        return repository.getSavedGroups()
    }
}
