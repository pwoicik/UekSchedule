package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

class GetAllScheduleEntries(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(): List<ScheduleEntry> {
        return repository.getAllScheduleEntries()
    }
}
