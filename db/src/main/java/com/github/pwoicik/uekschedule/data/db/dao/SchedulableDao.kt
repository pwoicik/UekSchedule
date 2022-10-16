package com.github.pwoicik.uekschedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableWithClassesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchedulableDao {

    @Query("select * from schedulables order by is_favorite desc, name")
    fun getAllSchedulables(): Flow<List<SchedulableEntity>>

    @Transaction
    @Query("select * from schedulables where id = :schedulableId")
    suspend fun getSchedulablesWithClasses(schedulableId: Long): SchedulableWithClassesEntity

    @Query(
        """
        select sum(count)
        from (
            select count(*) count
            from schedulables
            
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
    fun getSchedulablesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedulable(schedulable: SchedulableEntity)

    @Update
    suspend fun updateSchedulable(schedulable: SchedulableEntity)

    @Delete
    suspend fun deleteSchedulable(schedulable: SchedulableEntity)

    @Query("delete from schedulables where id = :schedulableId")
    suspend fun deleteSchedulable(schedulableId: Long)
}
