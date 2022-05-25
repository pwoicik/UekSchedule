package com.github.pwoicik.uekschedule.di

import android.content.Context
import com.github.pwoicik.uekschedule.common.domain.PreferencesManager
import com.github.pwoicik.uekschedule.repository.PreferencesManagerImpl
import com.github.pwoicik.uekschedule.repository.RepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
@Suppress("UNUSED")
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManagerImpl(context)
    }
}
