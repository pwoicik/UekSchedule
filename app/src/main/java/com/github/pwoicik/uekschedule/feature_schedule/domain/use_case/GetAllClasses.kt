package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetAllClasses(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<List<Class>> {
        return repository.getAllClassesFlow()
    }
}
