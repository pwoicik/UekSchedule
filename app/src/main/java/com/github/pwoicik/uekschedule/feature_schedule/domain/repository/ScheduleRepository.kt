package com.github.pwoicik.uekschedule.feature_schedule.domain.repository

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun addActivity(activity: Activity)

    suspend fun addGroup(group: Group)

    suspend fun deleteActivity(activity: Activity)

    suspend fun deleteGroup(group: Group)

    suspend fun getActivity(id: Long): Activity

    fun getAllActivities(): Flow<List<Activity>>

    suspend fun getAllGroups(): List<Group>

    fun getAllScheduleEntries(): Flow<List<ScheduleEntry>>

    suspend fun getSchedule(groupId: Long): List<ScheduleEntry>

    fun getSavedGroups(): Flow<List<Group>>

    fun getSavedGroupsCount(): Flow<Int>

    suspend fun refetchSchedules()
}
