package com.github.pwoicik.uekschedule.repository

import androidx.room.withTransaction
import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import com.github.pwoicik.uekschedule.data.api.ScheduleApi
import com.github.pwoicik.uekschedule.data.api.dto.SchedulableDto
import com.github.pwoicik.uekschedule.data.db.ScheduleDatabase
import com.github.pwoicik.uekschedule.data.db.entity.ActivityEntity
import com.github.pwoicik.uekschedule.data.db.entity.ClassEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableWithClassesEntity
import com.github.pwoicik.uekschedule.domain.model.*
import com.github.pwoicik.uekschedule.repository.mapper.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleApi: ScheduleApi,
    private val scheduleDatabase: ScheduleDatabase
) : ScheduleRepository {

    private val classDao = scheduleDatabase.classDao
    private val groupDao = scheduleDatabase.groupDao
    private val activityDao = scheduleDatabase.activityDao
    private val subjectDao = scheduleDatabase.subjectDao


    override suspend fun addSubjectToIgnored(subject: Subject) {
        subjectDao.insertSubject(subject.toSubjectEntity())
    }

    override suspend fun deleteActivity(activity: Activity) {
        activityDao.deleteActivity(activity.id)
    }

    override suspend fun deleteGroup(group: Schedulable) {
        groupDao.deleteSchedulable(group.id)
    }

    override suspend fun deleteSubjectFromIgnored(subject: Subject) {
        subjectDao.deleteSubject(subject.toSubjectEntity())
    }

    private suspend fun fetchGroupSchedule(group: Schedulable): SchedulableWithClassesEntity {
        return scheduleApi.getGroupSchedule(group.id).toGroupWithClasses()
    }

    private suspend fun fetchTeacherSchedule(teacher: Schedulable): SchedulableWithClassesEntity {
        return scheduleApi.getTeacherSchedule(teacher.id).toGroupWithClasses()
    }

    override suspend fun fetchSchedule(groupId: Long): Result<List<ScheduleEntry>> = try {
        Result.success(
            scheduleApi.getGroupSchedule(groupId)
                .toGroupWithClasses()
                .classes.map(ClassEntity::toScheduleEntry)
        )
    } catch (e: Exception) {
        Timber.e(e)
        Result.failure(e)
    }

    override suspend fun getActivity(id: Long): Activity {
        return activityDao.getActivity(id).toActivity()
    }

    override fun getAllActivities(): Flow<List<Activity>> {
        return activityDao.getAllActivities().map {
            it.map(ActivityEntity::toActivity)
        }
    }

    override suspend fun getAllSchedulables(type: SchedulableType): Result<List<Schedulable>> =
        try {
            Result.success(
                when (type) {
                    SchedulableType.Group -> scheduleApi.getGroups()
                    SchedulableType.Teacher -> scheduleApi.getTeachers()
                }.schedulables!!.map(SchedulableDto::toSchedulable)
            )
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }

    override fun getAllScheduleEntries(): Flow<List<ScheduleEntry>> {
        val classes = classDao.getAllClasses()
            .map { it.map(ClassEntity::toScheduleEntry) }
        val activities = activityDao.getAllActivities()
            .map {
                it.map(ActivityEntity::toScheduleEntries)
                    .flatten()
            }

        @Suppress("NAME_SHADOWING")
        return combine(classes, activities) { classes, activities ->
            (classes + activities).sortedBy(ScheduleEntry::startDateTime)
        }
    }

    override fun getAllSubjectsForGroup(groupId: Long): Flow<List<Subject>> = flow {
        val allSubjects = subjectDao.getAllSubjectsForGroup(groupId)

        subjectDao.getAllIgnoredSubjectsForGroup(groupId)
            .map { ignoredSubjects ->
                allSubjects.map { it.toSubject(it in ignoredSubjects) }
            }
            .collect(::emit)
    }

    override suspend fun getGroupWithClasses(group: Schedulable): SchedulableWithClasses {
        return groupDao.getSchedulablesWithClasses(group.id).toGroupWithClasses()
    }

    override fun getSavedGroups(): Flow<List<Schedulable>> {
        return groupDao.getAllSchedulables().map {
            it.map(SchedulableEntity::toSchedulable)
        }
    }

    override fun getSavedGroupsCount(): Flow<Int> {
        return groupDao.getSchedulablesCount()
    }

    override suspend fun saveActivity(activity: Activity) {
        activityDao.insertActivity(activity.toActivityEntity())
    }

    override suspend fun saveSchedulable(schedulable: Schedulable): Result<Unit> = try {
        val gwc = fetchGroupSchedule(schedulable)
        scheduleDatabase.withTransaction {
            groupDao.insertSchedulable(gwc.schedulable)
            classDao.insertAllClasses(gwc.classes)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Timber.e(e)
        Result.failure(e)
    }

    override suspend fun saveGroupWithClasses(gwc: SchedulableWithClasses) {
        scheduleDatabase.withTransaction {
            groupDao.insertSchedulable(gwc.group.toGroupEntity())
            classDao.insertAllClasses(gwc.classes.map(Class::toClassEntity))
        }
    }

    override suspend fun updateGroup(group: Schedulable) {
        groupDao.updateSchedulable(group.toGroupEntity())
    }

    override suspend fun updateSchedules(): Result<Unit> = try {
        val groups = getSavedGroups().first()
        val groupsWithClasses = groups.map { group ->
            Timber.d("fetching schedule for group ${group.name}")
            fetchGroupSchedule(group)
        }
        scheduleDatabase.withTransaction {
            classDao.deleteAllClasses()
            groupsWithClasses.forEach { (_, classes) ->
                classDao.insertAllClasses(classes)
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Timber.e(e)
        Result.failure(e)
    }
}
