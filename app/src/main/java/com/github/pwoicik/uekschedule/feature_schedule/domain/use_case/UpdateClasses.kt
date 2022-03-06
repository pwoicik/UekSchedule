package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class UpdateClasses(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<Resource<Unit>> = flow {
        Timber.d("updating schedules")
        emit(Resource.Loading())
        try {
            repository.updateSchedules()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Timber.d(e)
            emit(Resource.Error("Couldn't reach server."))
        }
    }
}
