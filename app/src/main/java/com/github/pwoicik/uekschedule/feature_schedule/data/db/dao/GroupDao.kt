package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Group
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.GroupWithClasses
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("select * from groups order by is_favorite desc, name")
    fun getAllGroups(): Flow<List<Group>>

    @Transaction
    @Query("select * from groups where id = :groupId")
    suspend fun getGroupWithClasses(groupId: Long): GroupWithClasses

    @Query(
        """
        select sum(count)
        from (
            select count(*) count
            from groups
            
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroup(group: Group)

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)
}
