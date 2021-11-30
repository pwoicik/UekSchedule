package com.github.pwoicik.uekschedule.feature_schedule.domain.repository

import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun getAllGroups(): List<Group>

    suspend fun getSavedGroups(): List<Group>

    fun getSavedGroupsFlow(): Flow<List<Group>>

    suspend fun deleteGroup(group: Group)

    fun getAllClassesFlow(): Flow<List<Class>>

    suspend fun addGroups(groups: List<Group>)
}
