package com.github.pwoicik.uekschedule.feature_schedule.domain.repository

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun getAllGroups(): List<Group>

    fun getSavedGroupsCount(): Flow<Int>

    fun getSavedGroups(): Flow<List<Group>>

    suspend fun deleteGroup(group: Group)

    suspend fun addGroup(group: Group)

    suspend fun refetchSchedules()

    suspend fun getActivity(id: Long): Activity

    fun getAllActivities(): Flow<List<Activity>>

    suspend fun addActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    fun getAllScheduleEntries(): Flow<List<ScheduleEntry>>
}
