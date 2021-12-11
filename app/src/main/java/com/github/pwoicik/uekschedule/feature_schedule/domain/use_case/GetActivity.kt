package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

class GetActivity(
    private val repository: ScheduleRepository
) {
    suspend operator fun invoke(id: Long): Activity {
        return repository.getActivity(id)
    }
}
