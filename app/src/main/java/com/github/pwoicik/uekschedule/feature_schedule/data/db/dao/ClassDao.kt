package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Query(
        """
        select *
        from classes c
        where
            group_id in (
                select id
                from groups
                where is_favorite = 1
            )
            and
            not exists (
                select 0
                from ignored_subjects i
                where
                    i.group_id = c.group_id
                    and
                    i.name = c.subject
                    and
                    i.type = c.type
            )
        """
    )
    fun getAllClasses(): Flow<List<Class>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClasses(classes: Collection<Class>)

    @Query("delete from classes")
    suspend fun deleteAllClasses()
}
