package uekschedule.mygroups.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uekschedule.browser.domain.model.Schedulable
import uekschedule.data.db.Database
import uekschedule.mygroups.domain.usecase.GetSchedulables

class GetSchedulablesImpl(
    private val db: Database,
) : GetSchedulables {
    override fun invoke(): Flow<ImmutableList<Schedulable>> =
        db.schedulableQueries
            .selectAll { id, name, type ->
                Schedulable(
                    id = when (type) {
                        0L -> Schedulable.Id.Group(id)
                        1L -> Schedulable.Id.Teacher(id)
                        else -> throw NotImplementedError()
                    },
                    name = name,
                )
            }
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map(::ImmutableListAdapter)
}
