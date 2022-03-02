package com.github.pwoicik.uekschedule.feature_schedule.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.pwoicik.uekschedule.feature_schedule.data.db.entity.Class
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface ClassDao {

    @Query("select * from classes")
    suspend fun getAllClasses(): List<Class>

    @Insert
    suspend fun insertAllClasses(classes: Collection<Class>)
}
