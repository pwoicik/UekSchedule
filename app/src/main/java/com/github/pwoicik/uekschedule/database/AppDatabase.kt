package com.github.pwoicik.uekschedule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Group::class, Class::class], version = 1)
@TypeConverters(ZonedDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "schedule_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance!!
            }
        }
    }
}
