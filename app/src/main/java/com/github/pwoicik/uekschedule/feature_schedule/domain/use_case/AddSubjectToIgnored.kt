package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Subject
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository

class AddSubjectToIgnored(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(subject: Subject) {
        repository.addSubjectToIgnored(subject)
    }
}
