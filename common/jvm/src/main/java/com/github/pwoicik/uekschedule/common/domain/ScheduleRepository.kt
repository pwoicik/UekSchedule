package com.github.pwoicik.uekschedule.common.domain

import com.github.pwoicik.uekschedule.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun addSubjectToIgnored(subject: Subject)

    suspend fun deleteActivity(activity: Activity)

    suspend fun deleteGroup(group: Schedulable)

    suspend fun deleteSubjectFromIgnored(subject: Subject)

    suspend fun getActivity(id: Long): Activity

    fun getAllActivities(): Flow<List<Activity>>

    suspend fun getAllSchedulables(type: SchedulableType): Result<List<Schedulable>>

    fun getAllScheduleEntries(): Flow<List<ScheduleEntry>>

    fun getAllSubjectsForGroup(groupId: Long): Flow<List<Subject>>

    suspend fun getGroupWithClasses(group: Schedulable): SchedulableWithClasses

    fun getSavedGroups(): Flow<List<Schedulable>>

    fun getSavedGroupsCount(): Flow<Int>

    suspend fun fetchSchedule(groupId: Long): Result<List<ScheduleEntry>>

    suspend fun saveActivity(activity: Activity)

    suspend fun saveSchedulable(schedulable: Schedulable): Result<Unit>

    suspend fun saveGroupWithClasses(gwc: SchedulableWithClasses)

    suspend fun updateGroup(group: Schedulable)

    suspend fun updateSchedules(): Result<Unit>
}
