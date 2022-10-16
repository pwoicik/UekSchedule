package com.github.pwoicik.uekschedule.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.pwoicik.uekschedule.data.db.converters.ScheduleDbTypeConverters
import com.github.pwoicik.uekschedule.data.db.dao.ActivityDao
import com.github.pwoicik.uekschedule.data.db.dao.ClassDao
import com.github.pwoicik.uekschedule.data.db.dao.SchedulableDao
import com.github.pwoicik.uekschedule.data.db.dao.SubjectDao
import com.github.pwoicik.uekschedule.data.db.entity.ActivityEntity
import com.github.pwoicik.uekschedule.data.db.entity.ClassEntity
import com.github.pwoicik.uekschedule.data.db.entity.SchedulableEntity
import com.github.pwoicik.uekschedule.data.db.entity.SubjectEntity
import com.github.pwoicik.uekschedule.data.db.migrations.Migration2to3

@Database(
    entities = [
        SchedulableEntity::class,
        ClassEntity::class,
        SubjectEntity::class,
        ActivityEntity::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2
        ),
        AutoMigration(
            from = 2,
            to = 3,
            spec = Migration2to3::class
        )
    ]
)
@TypeConverters(ScheduleDbTypeConverters::class)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract val classDao: ClassDao
    abstract val groupDao: SchedulableDao
    abstract val activityDao: ActivityDao
    abstract val subjectDao: SubjectDao
}
