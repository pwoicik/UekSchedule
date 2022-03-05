package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetAllScheduleEntries(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<List<ScheduleEntry>> {
        return repository.getAllScheduleEntries()
    }
}
