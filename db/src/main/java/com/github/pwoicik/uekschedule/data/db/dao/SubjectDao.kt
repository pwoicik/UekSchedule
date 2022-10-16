package com.github.pwoicik.uekschedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.data.db.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Query(
        """
        select c.schedulable_id, g.name as schedulable_name, c.subject as name, c.type
        from schedulables g
            inner join classes c
            on c.schedulable_id = g.id
        where g.id = :schedulableId
        group by g.name, c.subject, c.type
        order by c.subject
        """
    )
    suspend fun getAllSubjectsForGroup(schedulableId: Long): List<SubjectEntity>

    @Query("select * from ignored_subjects where schedulable_id = :schedulableId")
    fun getAllIgnoredSubjectsForGroup(schedulableId: Long): Flow<List<SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)
}
