package com.github.pwoicik.uekschedule.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.pwoicik.uekschedule.data.db.entity.ClassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Query(
        """
        select *
        from classes c
        where
            schedulable_id in (
                select id
                from schedulables
                where is_favorite = 1
            )
            and
            not exists (
                select 0
                from ignored_subjects i
                where
                    i.schedulable_id = c.schedulable_id
                    and
                    i.name = c.subject
                    and
                    i.type = c.type
            )
        """
    )
    fun getAllClasses(): Flow<List<ClassEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClasses(classes: Collection<ClassEntity>)

    @Query("delete from classes")
    suspend fun deleteAllClasses()
}
