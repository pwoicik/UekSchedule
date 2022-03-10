package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

class SaveActivity(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(activity: Activity) {
        repository.saveActivity(activity)
    }
}
