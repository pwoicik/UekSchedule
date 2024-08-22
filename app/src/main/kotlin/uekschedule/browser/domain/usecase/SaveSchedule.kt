package uekschedule.browser.domain.usecase

import uekschedule.browser.domain.model.Schedulable

interface SaveSchedule {
    suspend operator fun invoke(schedulable: Schedulable)
}
