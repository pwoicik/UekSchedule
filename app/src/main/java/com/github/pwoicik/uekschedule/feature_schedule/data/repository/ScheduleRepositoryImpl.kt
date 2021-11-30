package com.github.pwoicik.uekschedule.feature_schedule.data.repository

import androidx.room.withTransaction
import com.github.pwoicik.uekschedule.feature_schedule.data.api.ScheduleApi
import com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper.toGroup
import com.github.pwoicik.uekschedule.feature_schedule.data.api.mapper.toGroupWithClasses
import com.github.pwoicik.uekschedule.feature_schedule.data.db.ScheduleDatabase
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.GroupWithClasses
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl(
    private val scheduleApi: ScheduleApi,
    private val scheduleDatabase: ScheduleDatabase
) : ScheduleRepository {

    private val classDao = scheduleDatabase.classDao
    private val groupDao = scheduleDatabase.groupDao

    override suspend fun getAllGroups(): List<Group> {
        return scheduleApi.getGroups().groups!!.map { it.toGroup() }
    }

    override suspend fun getSavedGroups(): List<Group> {
        return groupDao.getAllGroups()
    }

    override fun getSavedGroupsFlow(): Flow<List<Group>> {
        return groupDao.getAllGroupsFlow()
    }

    override suspend fun deleteGroup(group: Group) {
        groupDao.deleteGroup(group)
    }

    override fun getAllClassesFlow(): Flow<List<Class>> {
        return classDao.getAllClasses().map {
            it.sortedBy(Class::startDateTime)
        }
    }

    override suspend fun addGroups(groups: List<Group>) {
        val groupsWithClasses = groups.map { group ->
            fetchSchedule(group)
        }
        scheduleDatabase.withTransaction {
            groupsWithClasses.forEach { (group, classes) ->
                groupDao.insertGroup(group)
                classDao.insertAllClasses(classes)
            }
        }
    }

    private suspend fun fetchSchedule(group: Group): GroupWithClasses {
        return scheduleApi.getSchedule(group.id).toGroupWithClasses()
    }
}
