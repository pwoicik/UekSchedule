package com.github.pwoicik.uekschedule.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ScheduleDao {

    @Transaction
    @Query("select * from schedules")
    abstract suspend fun getAllSchedules(): List<Schedule>

    @Transaction
    @Query("select * from classes")
    abstract suspend fun getAllClasses(): List<Class>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSchedule(schedule: Schedule)

    @Insert
    abstract suspend fun insertAllClasses(classes: Collection<Class>)

    @Transaction
    @Insert
    suspend fun insertScheduleWithClasses(swc: ScheduleWithClasses) {
        insertSchedule(swc.schedule)
        insertAllClasses(swc.classes)
    }
}
