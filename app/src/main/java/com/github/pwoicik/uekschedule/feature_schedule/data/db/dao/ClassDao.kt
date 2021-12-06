package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.*
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {

    @Query("select * from classes order by start_datetime")
    fun getAllClasses(): Flow<List<Class>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClasses(classes: Collection<Class>)
}
