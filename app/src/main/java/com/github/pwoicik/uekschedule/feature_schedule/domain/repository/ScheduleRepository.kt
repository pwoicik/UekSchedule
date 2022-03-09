package com.github.pwoicik.uekschedule.feature_schedule.domain.repository

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun deleteActivity(activity: Activity)

    suspend fun deleteGroup(group: Group)

    suspend fun getActivity(id: Long): Activity

    fun getAllActivities(): Flow<List<Activity>>

    suspend fun getAllGroups(): List<Group>

    fun getAllScheduleEntries(): Flow<List<ScheduleEntry>>

    suspend fun getGroupWithClasses(group: Group): GroupWithClasses

    fun getSavedGroups(): Flow<List<Group>>

    fun getSavedGroupsCount(): Flow<Int>

    suspend fun fetchSchedule(groupId: Long): List<ScheduleEntry>

    suspend fun saveActivity(activity: Activity)

    suspend fun saveGroup(group: Group)

    suspend fun saveGroupWithClasses(gwc: GroupWithClasses)

    suspend fun updateSchedules()
}
