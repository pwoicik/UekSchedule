package com.github.pwoicik.uekschedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.data.db.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Query(
        """
        select c.group_id, g.name as group_name, c.subject as name, c.type
        from groups g
            inner join classes c
            on c.group_id = g.id
        where g.id = :groupId
        group by g.name, c.subject, c.type
        order by c.subject
        """
    )
    suspend fun getAllSubjectsForGroup(groupId: Long): List<SubjectEntity>

    @Query("select * from ignored_subjects where group_id = :groupId")
    fun getAllIgnoredSubjectsForGroup(groupId: Long): Flow<List<SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity)
}
