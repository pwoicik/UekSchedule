package uekschedule.schedule.domain.usecase

import uekschedule.browser.domain.model.Schedular
import uekschedule.schedule.domain.model.Schedule

interface GetSchedule {
    suspend operator fun invoke(id: Schedular.Id) : Schedule
}
