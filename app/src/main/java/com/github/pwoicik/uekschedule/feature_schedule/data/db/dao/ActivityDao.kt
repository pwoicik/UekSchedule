package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("select * from activities where id = :id")
    suspend fun getActivity(id: Long): Activity

    @Query("select * from activities order by name")
    fun getAllActivities(): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)

    @Delete
    suspend fun deleteActivity(activity: Activity)
}
