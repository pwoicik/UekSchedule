package uekschedule.mygroups.domain.usecase

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import uekschedule.browser.domain.model.Schedulable

interface GetSchedulables {
    operator fun invoke(): Flow<ImmutableList<Schedulable>>
}
