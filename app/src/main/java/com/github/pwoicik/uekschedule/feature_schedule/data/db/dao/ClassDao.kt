package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Query("select * from classes")
    fun getAllClasses(): Flow<List<Class>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClasses(classes: Collection<Class>)

    @Query("delete from classes")
    suspend fun deleteAllClasses()
}
