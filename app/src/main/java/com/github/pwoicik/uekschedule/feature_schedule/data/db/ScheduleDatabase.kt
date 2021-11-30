package com.github.pwoicik.uekschedule.feature_schedule.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.pwoicik.uekschedule.feature_schedule.data.db.dao.ClassDao
import com.github.pwoicik.uekschedule.feature_schedule.data.db.dao.GroupDao
import com.github.pwoicik.uekschedule.feature_schedule.data.util.ZonedDateTimeConverter
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Class
import com.github.pwoicik.uekschedule.feature_schedule.domain.model.Group

@Database(
    entities = [Group::class, Class::class],
    version = 1
)
@TypeConverters(ZonedDateTimeConverter::class)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract val classDao: ClassDao
    abstract val groupDao: GroupDao
}
