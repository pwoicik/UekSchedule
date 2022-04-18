package com.github.pwoicik.uekschedule.di

import android.app.Application
import androidx.room.Room
import com.github.pwoicik.uekschedule.feature_schedule.common.Constants
import com.github.pwoicik.uekschedule.feature_schedule.data.api.ScheduleApi
import com.github.pwoicik.uekschedule.feature_schedule.data.db.ScheduleDatabase
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleModule {

    @Provides
    @Singleton
    fun provideScheduleApi(): ScheduleApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                TikXmlConverterFactory.create(
                    TikXml.Builder().exceptionOnUnreadXml(false).build()
                )
            )
            .build()
            .create(ScheduleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleDatabase(app: Application): ScheduleDatabase {
        return Room
            .databaseBuilder(
                app,
                ScheduleDatabase::class.java,
                Constants.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}
