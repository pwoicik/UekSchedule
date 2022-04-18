package com.github.pwoicik.uekschedule.feature_schedule.domain.repository

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun addSubjectToIgnored(subject: Subject)

    suspend fun deleteActivity(activity: Activity)

    suspend fun deleteGroup(group: Group)

    suspend fun deleteSubjectFromIgnored(subject: Subject)

    suspend fun getActivity(id: Long): Activity

    fun getAllActivities(): Flow<List<Activity>>

    suspend fun getAllGroups(): Result<List<Group>>

    fun getAllScheduleEntries(): Flow<List<ScheduleEntry>>

    fun getAllSubjectsForGroup(groupId: Long): Flow<List<Subject>>

    suspend fun getGroupWithClasses(group: Group): GroupWithClasses

    fun getSavedGroups(): Flow<List<Group>>

    fun getSavedGroupsCount(): Flow<Int>

    suspend fun fetchSchedule(groupId: Long): Result<List<ScheduleEntry>>

    suspend fun saveActivity(activity: Activity)

    suspend fun saveGroup(group: Group): Result<Unit>

    suspend fun saveGroupWithClasses(gwc: GroupWithClasses)

    suspend fun updateGroup(group: Group)

    suspend fun updateSchedules(): Result<Unit>
}
