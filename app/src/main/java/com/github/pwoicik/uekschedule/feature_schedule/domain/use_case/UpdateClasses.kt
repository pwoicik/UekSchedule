package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateClasses(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val savedGroups = repository.getSavedGroups()
            repository.addGroups(savedGroups)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't reach server."))
        }
    }
}
