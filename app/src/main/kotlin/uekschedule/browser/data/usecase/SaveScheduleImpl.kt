package uekschedule.browser.data.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uekschedule.browser.domain.model.Schedulable
import uekschedule.browser.domain.usecase.SaveSchedule
import uekschedule.data.db.Database

class SaveScheduleImpl(
    private val db: Database,
) : SaveSchedule {
    override suspend fun invoke(schedulable: Schedulable) = withContext(Dispatchers.IO) {
        db.schedulableQueries.insert(
            uekschedule.data.db.Schedulable(
                id = schedulable.id.value,
                name = schedulable.name,
                type = when (schedulable.id) {
                    is Schedulable.Id.Group -> 0
                    is Schedulable.Id.Teacher -> 1
                },
            ),
        )
    }
}
