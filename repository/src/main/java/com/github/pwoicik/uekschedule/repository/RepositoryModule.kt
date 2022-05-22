package com.github.pwoicik.uekschedule.repository

import com.github.pwoicik.uekschedule.common.domain.ScheduleRepository
import com.github.pwoicik.uekschedule.data.api.ApiModule
import com.github.pwoicik.uekschedule.data.db.DbModule
import dagger.Binds
import dagger.Module

@Module(includes = [ApiModule::class, DbModule::class])
abstract class RepositoryModule {

    @Binds
    @Suppress("UNUSED")
    internal abstract fun bindRepository(impl: ScheduleRepositoryImpl): ScheduleRepository
}
