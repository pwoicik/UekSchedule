package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("select * from groups order by name")
    fun getAllGroups(): Flow<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)
}
