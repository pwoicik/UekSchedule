package com.github.pwoicik.uekschedule.di

import com.github.pwoicik.uekschedule.feature_schedule.data.repository.ScheduleRepositoryImpl
import com.github.pwoicik.uekschedule.feature_schedule.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(impl: ScheduleRepositoryImpl): ScheduleRepository
}
