package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SaveGroup(
    private val repository: ScheduleRepository
) {

    operator fun invoke(group: Group) = flow {
        emit(Resource.Loading())
        try {
            repository.saveGroup(group)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Timber.tag("group ${group.name}").e(e)
            emit(Resource.Error(e.message ?: "Couldn't reach server."))
        }
    }
}
