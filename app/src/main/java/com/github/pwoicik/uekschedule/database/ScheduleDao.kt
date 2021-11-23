package com.github.pwoicik.uekschedule.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ScheduleDao {

    @Transaction
    @Query("select * from groups")
    abstract fun getAllGroups(): Flow<List<Group>>

    @Transaction
    @Query("select * from classes")
    abstract fun getAllClasses(): Flow<List<Class>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGroup(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllClasses(classes: Collection<Class>)

    @Transaction
    @Insert
    suspend fun insertGroupWithClasses(gwc: GroupWithClasses) {
        insertGroup(gwc.group)
        insertAllClasses(gwc.classes)
    }
}
