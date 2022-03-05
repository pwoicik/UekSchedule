package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetScheduleForGroup(
    private val repository: ScheduleRepository
) {

    operator fun invoke(groupId: Long) = flow {
        emit(Resource.Loading())
        try {
            val entries = repository.getSchedule(groupId)
            emit(Resource.Success(entries))
        } catch (e: Exception) {
            Timber.tag("group $groupId").e(e)
            emit(Resource.Error(e.message ?: "Couldn't reach server."))
        }
    }
}
