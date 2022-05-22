package com.github.pwoicik.uekschedule.di

import com.github.pwoicik.uekschedule.repository.RepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
@Suppress("UNUSED")
object AppModule
