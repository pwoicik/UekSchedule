package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RefreshClasses(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            Timber.d("refetching schedules")
            repository.refetchSchedules()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Timber.d("Error during refetching schedules")
            emit(Resource.Error("Couldn't reach server."))
        }
    }
}
