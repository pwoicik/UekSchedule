package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

class SaveGroupWithClasses(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(gwc: GroupWithClasses) {
        repository.saveGroupWithClasses(gwc)
    }
}
