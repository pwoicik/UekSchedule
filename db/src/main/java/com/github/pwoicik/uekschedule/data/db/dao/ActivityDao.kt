package com.github.pwoicik.uekschedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.data.db.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("select * from activities where id = :id")
    suspend fun getActivity(id: Long): ActivityEntity

    @Query("select * from activities order by id desc")
    fun getAllActivities(): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    @Query("delete from activities where id = :activityId")
    suspend fun deleteActivity(activityId: Long)
}
