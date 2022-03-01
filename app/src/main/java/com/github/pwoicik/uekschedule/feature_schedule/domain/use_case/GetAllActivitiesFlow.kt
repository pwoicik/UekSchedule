package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetAllActivitiesFlow(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<List<Activity>> {
        return repository.getAllActivitiesFlow()
    }
}
