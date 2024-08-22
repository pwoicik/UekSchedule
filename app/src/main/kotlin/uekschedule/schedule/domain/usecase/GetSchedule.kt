package uekschedule.schedule.domain.usecase

import uekschedule.browser.domain.model.Schedulable
import uekschedule.schedule.domain.model.Schedule

interface GetSchedule {
    suspend operator fun invoke(id: Schedulable.Id): Schedule
}
