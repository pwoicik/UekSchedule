package com.github.pwoicik.uekschedule.feature_schedule.domain.repository

import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.ScheduleEntry
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun getAllGroups(): List<Group>

    fun getSavedGroupsCount(): Flow<Int>

    fun getSavedGroups(): Flow<List<Group>>

    suspend fun deleteGroup(group: Group)

    fun getAllClasses(): Flow<List<Class>>

    suspend fun addGroups(groups: List<Group>)

    suspend fun getActivity(id: Long): Activity

    suspend fun getAllActivities(): List<Activity>

    fun getAllActivitiesFlow(): Flow<List<Activity>>

    suspend fun addActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun getAllScheduleEntries(): List<ScheduleEntry>
}
