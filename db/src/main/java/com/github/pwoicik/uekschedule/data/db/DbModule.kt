package com.github.pwoicik.uekschedule.data.db

import android.app.Application
import androidx.room.Room
import com.github.pwoicik.uekschedule.common.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ) = Room
        .databaseBuilder(
            app,
            ScheduleDatabase::class.java,
            Constants.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
}
