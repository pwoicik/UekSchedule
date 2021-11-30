package com.github.pwoicik.uekschedule.feature_schedule.domain.use_case

import com.github.pwoicik.uekschedule.common.Resource
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllGroups(
    private val repository: ScheduleRepository
) {

    operator fun invoke(): Flow<Resource<List<Group>>> = flow {
        emit(Resource.Loading())
        try {
            val groups = repository.getAllGroups()
            emit(Resource.Success(groups))
        } catch (e: Exception) {
            emit(Resource.Error("Couldn't reach server."))
        }
    }
}
