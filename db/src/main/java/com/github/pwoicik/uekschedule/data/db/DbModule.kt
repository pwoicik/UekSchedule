package com.github.pwoicik.uekschedule.data.db

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.github.pwoicik.uekschedule.common.Constants
import com.github.pwoicik.uekschedule.common.domain.PreferencesManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        preferences: PreferencesManager
    ): ScheduleDatabase {
        clearTablesIfNeeded(app, preferences)
        return Room
            .databaseBuilder(
                app,
                ScheduleDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Migration2to3 doesn't work when renaming column ignored_subjects.group_id to schedulable_id
     * as it fills this column with null values for some reason.
     * To work around it this function clears that table before creating room instance.
     * TODO: remove after all users migrate from ver. 33
     */
    private fun clearTablesIfNeeded(
        app: Application,
        preferences: PreferencesManager
    ) {
        runBlocking {
            val lastUsedAppVersion = preferences.lastUsedAppVersion.first() ?: 0
            if (lastUsedAppVersion < 35) {
                app.openOrCreateDatabase(Constants.DATABASE_NAME, Context.MODE_PRIVATE, null).use {
                    it.execSQL("delete from ignored_subjects")
                }
            }
        }
    }
}
