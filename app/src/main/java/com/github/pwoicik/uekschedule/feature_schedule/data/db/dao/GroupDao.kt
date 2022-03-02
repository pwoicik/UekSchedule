package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("select * from groups order by name")
    fun getAllGroups(): Flow<List<Group>>

    @Query(
        """
        select sum(count) from (
            select count(*) count from groups
            union all
            select count(*) count
                from activities
                where 
                    repeat_on_days_of_week is not null
                    or
                    start_datetime >= strftime('%s', date(strftime('now')))
        )
    """
    )
    fun getGroupsCount(): Flow<Int>

    @Insert
    suspend fun insertGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)
}
